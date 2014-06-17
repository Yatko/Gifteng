/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.config.Constants;
import com.venefica.config.EmailConfig;
import com.venefica.dao.UserSettingDao;
import com.venefica.model.ImageType;
import com.venefica.model.MemberUserData;
import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.model.UserSetting;
import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DataSourceResolver;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.resolver.DataSourceCompositeResolver;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.app.event.IncludeEventHandler;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 *
 * @author gyuszi
 */
@Named
public class EmailSender {
    
    private static final Log logger = LogFactory.getLog(EmailSender.class);
    
    private static final String TEMPLATES_FOLDER = "templates/";
    
    @Inject
    private EmailConfig emailConfig;
    @Inject
    private UserSettingDao userSettingDao;
    
    private VelocityEngine velocityEngine;
    private DataSourceResolver dataSourceResolver;
    
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
        
        List<DataSourceResolver> resolvers = new ArrayList<DataSourceResolver>(0);
        if ( emailConfig.getImagesBaseUrls() != null && emailConfig.getImagesBaseUrls().length > 0 ) {
            for ( String imagesBaseUrl : emailConfig.getImagesBaseUrls() ) {
                try {
                    DataSourceResolver resolver = new DataSourceUrlResolver(new URL(imagesBaseUrl));
                    resolvers.add(resolver);
                } catch ( MalformedURLException ex ) {
                    logger.error("The given image base URL (" + imagesBaseUrl + ") is invalid.", ex);
                }
            }
        }
        
        dataSourceResolver = new DataSourceCompositeResolver(resolvers.toArray(new DataSourceResolver[resolvers.size()]), true);
    }
    
    public boolean sendNotification(NotificationType notificationType, User user, Map<String, Object> vars) {
        return sendNotification(notificationType, null, user, vars, null);
    }
    
    public boolean sendNotification(NotificationType notificationType, User user, Map<String, Object> vars, Map<File, ImageType> attachments) {
        return sendNotification(notificationType, null, user, vars, attachments);
    }
    
    public boolean sendNotification(NotificationType notificationType, String subtype, User user, Map<String, Object> vars) {
        return sendNotification(notificationType, subtype, user, vars, null);
    }
    
    /**
     * Send notification email to the given user if is configured for the
     * specified notification type.
     * 
     * @param notificationType
     * @param user
     * @param vars 
     * @return true upon send success
     */
    public boolean sendNotification(NotificationType notificationType, String subtype, User user, Map<String, Object> vars, Map<File, ImageType> attachments) {
        if ( user.isBusinessAccount() ) {
            logger.info("User (id: " + user.getId() + ") is a business account, no notification mail will be sent.");
            return false;
        }
        
        try {
            if ( canSend(notificationType, user) ) {
                sendHtmlEmailByTemplates(
                        notificationType.getVelocityTemplatePath(subtype),
                        user.getEmail(),
                        vars,
                        attachments
                        );
            }
        } catch ( MailException ex ) {
            logger.error("Could not send notification email (email: " + user.getEmail() + ", type: " + notificationType + ")", ex);
            return false;
        }
        return true;
    }
    
    public boolean sendNotification(NotificationType notificationType, String email, Map<String, Object> vars) {
        return sendNotification(notificationType, null, email, vars, null);
    }
    
    public boolean sendNotification(NotificationType notificationType, String email, Map<String, Object> vars, Map<File, ImageType> attachments) {
        return sendNotification(notificationType, null, email, vars, attachments);
    }
    
    public boolean sendNotification(NotificationType notificationType, String subtype, String email, Map<String, Object> vars) {
        return sendNotification(notificationType, subtype, email, vars, null);
    }
    
    public boolean sendNotification(NotificationType notificationType, String subtype, String email, Map<String, Object> vars, Map<File, ImageType> attachments) {
        try {
            if ( canSend(notificationType, null) ) {
                sendHtmlEmailByTemplates(
                        notificationType.getVelocityTemplatePath(subtype),
                        email,
                        vars,
                        attachments
                        );
            }
        } catch ( MailException ex ) {
            logger.error("Could not send notification email (email: " + email + ", type: " + notificationType + ")", ex);
            return false;
        }
        return true;
    }
    
    /**
     * Generates the content (subject, html message, plain text message)
     * of the given templates and sends html mail to the provided address.
     * 
     * @param templatePath velocity template path
     * @param email email address
     * @param vars velocity context variables
     * @throws MailException 
     */
    private void sendHtmlEmailByTemplates(String templatePath, String email, Map<String, Object> vars, Map<File, ImageType> attachments) throws MailException {
        String subject = mergeVelocityTemplate(templatePath, "subject.vm", vars);
        String htmlMessage = mergeVelocityTemplate(templatePath, "message.html.vm", vars);
        String plainMessage = mergeVelocityTemplate(templatePath, "message.txt.vm", vars);
        
        sendHtmlEmail(subject, htmlMessage, plainMessage, email, attachments);
    }
    
    /**
     * Send a html message to the given address.
     * 
     * @param subject email subject
     * @param htmlMessage html message content
     * @param textMessage plain text message (in case that the recipient does
     * not support html)
     * @param toEmailAddress recipient email address
     * @throws MailException can be thrown in multiple cases: wrong email address,
     * invalid message, error when sending
     */
    protected void sendHtmlEmail(String subject, String htmlMessage, String textMessage, String toEmailAddress, Map<File, ImageType> attachments) throws MailException {
        if ( !emailConfig.isEmailEnabled() ) {
            logger.info("Email sending is not enabled!");
            return;
        }
        
        MultiPartEmail email = createEmail(subject, htmlMessage, textMessage);
        try {
            email.addTo(toEmailAddress);
        } catch ( EmailException ex ) {
            logger.error("Invalid 'to' (" + toEmailAddress + ") address", ex);
            throw new MailException(MailException.INVALID_TO_ADDRESS, ex);
        }
        
        if ( attachments != null && !attachments.isEmpty() ) {
            for ( Map.Entry<File, ImageType> entry : attachments.entrySet() ) {
                File attachment = entry.getKey();
                ImageType imageType = entry.getValue();
                
                if ( attachment == null || !attachment.isFile() || !attachment.exists() || attachment.length() == 0 ) {
                    logger.debug("Skipping file attachment as is not as expected (attachment: " + attachment + ")");
                    continue;
                }
                
                try {
                    String name = attachment.getName() + (imageType != null ? "." + imageType.getFormatName() : "");
                    email.attach(new FileDataSource(attachment), name, null);
                } catch ( EmailException ex ) {
                    logger.error("Cannot attach file (name: " + attachment.getName() + ")", ex);
                }
            }
        }
        
        try {
            email.send();
        } catch ( EmailException ex ) {
            logger.error("Cannot send email", ex);
            throw new MailException(MailException.EMAIL_SEND_ERROR, ex);
        }
    }
    
    // internal helpers
    
    private MultiPartEmail createEmail(String subject, String htmlMessage, String textMessage) throws MailException {
        try {
            MultiPartEmail email;
            if ( htmlMessage != null && !htmlMessage.trim().isEmpty() ) {
                email = new ImageHtmlEmail();
                ((ImageHtmlEmail) email).setDataSourceResolver(dataSourceResolver);
                ((ImageHtmlEmail) email).setHtmlMsg(htmlMessage);
                if ( textMessage != null && !textMessage.trim().isEmpty() ) {
                    ((ImageHtmlEmail) email).setTextMsg(textMessage);
                }
            } else if ( textMessage != null && !textMessage.trim().isEmpty() ) {
                email = new MultiPartEmail();
                email.setMsg(textMessage);
            } else {
                throw new MailException("Cannot send empty email");
            }
            
            email.setSubject(subject != null ? subject.trim() : "");
            email.setHostName(emailConfig.getHostName());
            email.setSmtpPort(emailConfig.getSmtpPort());
            email.setSslSmtpPort(Integer.toString(emailConfig.getSmtpPortSSL()));
            email.setAuthenticator(new DefaultAuthenticator(emailConfig.getUsername(), emailConfig.getPassword()));
            email.setSSLOnConnect(emailConfig.isUseSSL());
            email.setCharset(emailConfig.getCharset());
            email.setBounceAddress(emailConfig.getUndeliveredEmailAddress());
            try {
                email.setFrom(emailConfig.getFromEmailAddress(), emailConfig.getFromName(), emailConfig.getCharset());
            } catch ( EmailException ex ) {
                logger.error("Invalid 'from' (" + emailConfig.getFromEmailAddress() + ") address", ex);
                throw new MailException(MailException.INVALID_FROM_ADDRESS, ex);
            }
            return email;
        } catch (EmailException ex) {
            logger.error("Erronous email message", ex);
            throw new MailException(MailException.INVALID_EMAIL_MESSAGE, ex);
        }
    }
    
    private boolean canSend(NotificationType notificationType, User user) {
        boolean canSend = notificationType.isSystemNotification();
        if ( !canSend && user != null ) {
            //system notifications will always generate emails, the rest depends
            //on user configurations
            MemberUserData userData = (MemberUserData) user.getUserData();
            UserSetting userSetting = (userData != null && userData.getUserSetting() != null) ? userSettingDao.get(userData.getUserSetting().getId()) : null;
            canSend = userSetting != null && userSetting.notificationExists(notificationType);
        }
        return canSend;
    }
    
    private String mergeVelocityTemplate(final String templatePath, String templateName, Map<String, Object> vars) {
        if ( velocityEngine == null ) {
            throw new RuntimeException("Velocity engine is not initialized");
        }
        
        try {
            Template template = velocityEngine.getTemplate(TEMPLATES_FOLDER + templatePath + templateName);
            VelocityContext context = new VelocityContext();
            context.put("baseUrl", emailConfig.getBaseUrl()); //global key available for all templates
            
            if ( vars != null && !vars.isEmpty() ) {
                for ( Map.Entry<String, Object> entry : vars.entrySet() ) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    context.put(key, value);
                }
            }
            
            EventCartridge eventCartridge = new EventCartridge();
            eventCartridge.addIncludeEventHandler(new IncludeEventHandler() {
                @Override
                public String includeEvent(String includeResourcePath, String currentResourcePath, String directiveName) {
                    if ( velocityEngine.resourceExists(includeResourcePath) ) {
                        return includeResourcePath;
                    }
                    return TEMPLATES_FOLDER + templatePath + includeResourcePath;
                }
            });
            eventCartridge.attachToContext(context);

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
