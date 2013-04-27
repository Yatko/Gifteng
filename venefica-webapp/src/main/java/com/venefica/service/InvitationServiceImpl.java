/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.common.EmailSender;
import com.venefica.common.MailChimpSender;
import com.venefica.common.MailException;
import com.venefica.common.RandomGenerator;
import com.venefica.config.Constants;
import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import static com.venefica.service.AbstractService.logger;
import com.venefica.service.dto.InvitationDto;
import com.venefica.service.fault.InvitationException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
//import java.util.UUID;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Service("invitationService")
@WebService(endpointInterface = "com.venefica.service.InvitationService")
public class InvitationServiceImpl extends AbstractService implements InvitationService {

    private static final int EXPIRATION_PERIOD_DAYS = 3;
    private static final int CODE_LENGTH = 4;
    
    private static final String TEMPLATES_FOLDER = "templates/";
    private static final String INVITATION_REQUEST_SUBJECT_TEMPLATE = TEMPLATES_FOLDER + "invitation-request-subject.vm";
    private static final String INVITATION_REQUEST_HTML_MESSAGE_TEMPLATE = TEMPLATES_FOLDER + "invitation-request-message.html.vm";
    private static final String INVITATION_REQUEST_PLAIN_MESSAGE_TEMPLATE = TEMPLATES_FOLDER + "invitation-request-message.txt.vm";
    
    @Inject
    private InvitationDao invitationDao;
    @Inject
    private EmailSender emailSender;
    @Inject
    private MailChimpSender mailChimpSender;
    
    private VelocityEngine velocityEngine;
    
    @PostConstruct
    public void init() {
        try {
            Properties props = new Properties();
            props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
            props.setProperty("runtime.log.logsystem.log4j.category", "velocity");
            props.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
            props.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty(RuntimeConstants.INPUT_ENCODING, Constants.DEFAULT_CHARSET);
            props.setProperty(RuntimeConstants.OUTPUT_ENCODING, Constants.DEFAULT_CHARSET);
            
            for ( String name : props.stringPropertyNames() ) {
                String value = props.getProperty(name);
                logger.debug("Setting property for velocity initialization (" + name + "=" + value + ")");
            }
            
            velocityEngine = new VelocityEngine(props);
            velocityEngine.init();
            logger.info("Velocity engine initialized");
        } catch ( Exception ex ) {
            logger.error(ex.getClass().getSimpleName() + " thrown when trying to initialize the velocity engine", ex);
        }
    }
    
    @Override
    @Transactional
    public Long requestInvitation(InvitationDto invitationDto) throws InvitationException {
        Invitation invitation = new Invitation();
        invitationDto.update(invitation);
        
        String code;
        int generationTried = 0;
        while ( true ) {
            code = RandomGenerator.generateNumeric(CODE_LENGTH);
            generationTried++;
            if ( invitationDao.findByCode(code) == null ) {
                //the generated code does not exists, found an unused (free) one
                break;
            } else if ( generationTried >= 10 ) {
                throw new InvitationException("Cannot generate valid invitation code!");
            }
        }
        
        Date expiresAt = DateUtils.addDays(new Date(), EXPIRATION_PERIOD_DAYS);
        invitation.setExpiresAt(expiresAt);
        invitation.setExpired(false);
        invitation.setCode(code);
        invitation.setIpAddress(getIpAddress());
        
        Long invitationId = invitationDao.save(invitation);
        
        if ( emailSender.isEnabled() ) {
            try {
                String subject = mergeVelocityTemplate(INVITATION_REQUEST_SUBJECT_TEMPLATE, code);
                String htmlMessage = mergeVelocityTemplate(INVITATION_REQUEST_HTML_MESSAGE_TEMPLATE, code);
                String plainMessage = mergeVelocityTemplate(INVITATION_REQUEST_PLAIN_MESSAGE_TEMPLATE, code);

                emailSender.sendHtmlEmail(subject, htmlMessage, plainMessage, invitationDto.getEmail());
            } catch ( MailException ex ) {
                logger.error("Email exception", ex);
                throw new InvitationException(ex.getErrorCode(), "Could not send invitation mail!");
            } catch ( Exception ex ) {
                logger.error("Runtime exception", ex);
                throw new InvitationException(MailException.GENERAL_ERROR, ex.getMessage());
            }
        }
        if ( mailChimpSender.isEnabled() ) {
            try {
                String source;
                if ( invitationDto.getOtherSource() != null && !invitationDto.getOtherSource().trim().isEmpty() ) {
                    source = invitationDto.getOtherSource();
                } else {
                    source = invitationDto.getSource();
                }
                
                Map<String, Object> vars = new HashMap<String, Object>(0);
                vars.put("COUNTRY", invitationDto.getCountry());
                vars.put("ZIPCODE", invitationDto.getZipCode());
                vars.put("USERTYPE", invitationDto.getUserType().getDescription());
                vars.put("SOURCE", source);
                
                mailChimpSender.listSubscribe(invitationDto.getEmail(), vars);
            } catch ( MailException ex ) {
                logger.error("MailChimp exception", ex);
                throw new InvitationException(ex.getErrorCode(), "Could not list subscribe to MailChimp!");
            }
        }
        
        return invitationId;
    }
    
    @Override
    public boolean isInvitationValid(String code) {
        Invitation invitation = invitationDao.findByCode(code);
        
        if ( invitation == null ) {
            logger.debug("Invitation (code: " + code + ") could not be found.");
            return false;
        } else if ( !invitation.isValid()) {
            logger.warn("Invitation (code: " + code + ") is not valid anymore.");
            return false;
        }
        
        return true;
    }
    
    // internal helpers
    
    private String mergeVelocityTemplate(String templateName, String invitationCode) {
        if ( velocityEngine == null ) {
            throw new RuntimeException("Velocity engine is not initialized");
        }
        
        try {
            Template template = velocityEngine.getTemplate(templateName);
            VelocityContext context = new VelocityContext();
            context.put("invitationCode", invitationCode);

            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            return sw.toString();
        } catch ( ResourceNotFoundException ex ) {
            throw new RuntimeException("Resource (" + templateName + ") not found", ex);
        } catch ( ParseErrorException ex ) {
            throw new RuntimeException("Syntax error! Problem parsing resource (" + templateName + ")", ex);
        } catch ( MethodInvocationException ex ) {
            throw new RuntimeException("Invocation error on resource (" + templateName + ")", ex);
        } catch ( TemplateInitException ex ) {
            throw new RuntimeException("Initialization error of resource (" + templateName + ")", ex);
        } catch ( Exception ex ) {
            throw new RuntimeException("Exception thrown on resource (" + templateName + "): " + ex.getMessage(), ex);
        }
    }
}
