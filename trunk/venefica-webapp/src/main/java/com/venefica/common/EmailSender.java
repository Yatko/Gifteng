/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.config.Constants;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 *
 * @author gyuszi
 */
public class EmailSender {
    
    private static final Log logger = LogFactory.getLog(EmailSender.class);
    
    private static final String TEMPLATES_FOLDER = "templates/";
    
    private int smtpPort;
    private int smtpPortSSL;
    private String charset;
    private String hostName;
    private String username;
    private String password;
    private boolean useSSL;
    private String fromEmailAddress;
    private String fromName;
    private String undeliveredEmailAddress;
    private String imagesBaseUrl;
    private boolean enabled;
    
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
    
    /**
     * Returns the enabled status of this service.
     * @return 
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Generates the content (subject, html message, plain text message)
     * of the given templates and sends html mail to the provided address.
     * 
     * @param subjectTemplate velocity template name for mail subject
     * @param htmlMessageTemplate velocity template name for mail html mail message
     * @param plainMessageTemplate velocity template name for mail plain text message
     * @param email email address
     * @param vars velocity context variables
     * @throws MailException 
     */
    public void sendHtmlEmailByTemplates(String subjectTemplate, String htmlMessageTemplate, String plainMessageTemplate, String email, Map<String, Object> vars) throws MailException {
        String subject = mergeVelocityTemplate(TEMPLATES_FOLDER + subjectTemplate, vars);
        String htmlMessage = mergeVelocityTemplate(TEMPLATES_FOLDER + htmlMessageTemplate, vars);
        String plainMessage = mergeVelocityTemplate(TEMPLATES_FOLDER + plainMessageTemplate, vars);
        
        sendHtmlEmail(subject, htmlMessage, plainMessage, email);
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
     * invalid message
     */
    public void sendHtmlEmail(String subject, String htmlMessage, String textMessage, String toEmailAddress) throws MailException {
        if ( !enabled ) {
            logger.info("Email sending is not enabled!");
            return;
        }
        
        ImageHtmlEmail email = new ImageHtmlEmail();
        try {
            email.setDataSourceResolver(new DataSourceUrlResolver(new URL(imagesBaseUrl)));
        } catch ( MalformedURLException ex ) {
            logger.error("The given image base URL (" + imagesBaseUrl + ") is invalid. Email sending is not skipped.", ex);
        }
        email.setHostName(hostName);
        email.setSmtpPort(smtpPort);
        email.setSslSmtpPort(Integer.toString(smtpPortSSL));
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(useSSL);
        email.setCharset(charset);
        email.setBounceAddress(undeliveredEmailAddress);
        try {
            email.setFrom(fromEmailAddress, fromName, charset);
        } catch ( EmailException ex ) {
            logger.error("Invalid 'from' (" + fromEmailAddress + ") address", ex);
            throw new MailException(MailException.INVALID_FROM_ADDRESS, ex);
        }
        try {
            email.addTo(toEmailAddress);
        } catch ( EmailException ex ) {
            logger.error("Invalid 'to' (" + toEmailAddress + ") address", ex);
            throw new MailException(MailException.INVALID_TO_ADDRESS, ex);
        }
        try {
            email.setSubject(subject);
            email.setHtmlMsg(htmlMessage);
            email.setTextMsg(textMessage);
        } catch ( EmailException ex ) {
            logger.error("Erronous email message", ex);
            throw new MailException(MailException.INVALID_EMAIL_MESSAGE, ex);
        }
        try {
            email.send();
        } catch ( EmailException ex ) {
            logger.error("Cannot send email", ex);
            throw new MailException(MailException.EMAIL_SEND_ERROR, ex);
        }
    }
    
    public String mergeVelocityTemplate(String templateName, Map<String, Object> vars) {
        if ( velocityEngine == null ) {
            throw new RuntimeException("Velocity engine is not initialized");
        }
        
        try {
            Template template = velocityEngine.getTemplate(templateName);
            VelocityContext context = new VelocityContext();
            if ( vars != null && !vars.isEmpty() ) {
                for ( Map.Entry<String, Object> entry : vars.entrySet() ) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    context.put(key, value);
                }
            }

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

    // setters
    
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpPortSSL(int smtpPortSSL) {
        this.smtpPortSSL = smtpPortSSL;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setUndeliveredEmailAddress(String undeliveredEmailAddress) {
        this.undeliveredEmailAddress = undeliveredEmailAddress;
    }

    public void setImagesBaseUrl(String imagesBaseUrl) {
        this.imagesBaseUrl = imagesBaseUrl;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
