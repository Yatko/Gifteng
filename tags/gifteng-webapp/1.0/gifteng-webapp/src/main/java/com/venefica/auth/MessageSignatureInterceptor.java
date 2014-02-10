/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.auth;

import com.venefica.config.Constants;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.ProtocolException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.MessageInfo;
import org.w3c.dom.Element;

/**
 * The message signing and verifying interceptor. Verifies any incoming requests
 * and sign any outgoing. This is an important step to not allow unwanted messages
 * into system.
 * 
 * TODO: currently there is not fully functional, so is disabled temporary.
 * 
 * @author gyuszi
 */
public class MessageSignatureInterceptor extends AbstractPhaseInterceptor<Message> {
    
    private final Log logger = LogFactory.getLog(MessageSignatureInterceptor.class);
    
    @Inject
    private MessageEncryptor messageEncryptor;
    
    private boolean enabled = false;

    public MessageSignatureInterceptor() {
        super(Phase.PRE_LOGICAL);
    }
    
    @Override
    public void handleMessage(Message message) throws Fault {
        if ( !enabled ) {
            return;
        }
        
        try {
            if ( isOutbound(message) ) {
                //sign message
                String generatedSignature = createSignature(isRequestor(message), message);
                createSoapHeaderSignature(message, generatedSignature);
                logger.debug("Generated request signature (sign): " + generatedSignature);
            } else {
                //verify signature
                String extractedSignature = extractSignature(message);
                String generatedSignature = createSignature(true, message);
                logger.debug("Extracted signature (verify): " + extractedSignature);
                logger.debug("Generated request signature (verify): " + generatedSignature);
                if ( !extractedSignature.equals(generatedSignature) ) {
                    logger.warn("The extracted (" + extractedSignature + ") and generated (" + generatedSignature + ") signatures does not match");
                    throw new ProtocolException("Invalid signature (the extracted and generated does not match)");
                }
                generatedSignature = createSignature(false, message);
                createSoapHeaderSignature(message, generatedSignature);
                logger.debug("Generated response signature (verify): " + generatedSignature);
            }
        } catch ( Exception ex ) {
            logger.error("Exception thrown when operating with message signature", ex);
            throw new Fault(ex);
        }
    }
    
    private boolean isOutbound(Message message) {
        return MessageUtils.isOutbound(message);
    }
    
    private String createSignature(boolean outbound, Message message) throws UnsupportedEncodingException {
        return messageEncryptor.generateMessageSignature(getOperation(outbound, message));
    }
    
    private String getOperation(boolean outbound, Message message) {
        String operation;
        if ( isOutbound(message) ) {
            MessageInfo messageInfo = (MessageInfo) message.getExchange().getOutMessage().get("org.apache.cxf.service.model.MessageInfo");
            if ( outbound ) {
                operation = messageInfo.getOperation().getInputName();
            } else {
                operation = messageInfo.getOperation().getOutputName();
            }
        } else {
            MessageInfo messageInfo = (MessageInfo) message.getExchange().getInMessage().get("org.apache.cxf.service.model.MessageInfo");
            if ( outbound ) {
                operation = messageInfo.getOperation().getInputName();
            } else {
                operation = messageInfo.getOperation().getOutputName();
            }
        }
        
        //String operation = message.getExchange().getBindingOperationInfo().getName().getLocalPart();
        return operation;
    }
    
    private String extractSignature(Message message) {
        Header header = findHeader(message);
        if ( header != null ) {
            return ((Element) header.getObject()).getTextContent();
        }
        return null;
    }
    
    private void createSoapHeaderSignature(Message message, String signature) throws JAXBException {
        Header header = findHeader(message);
        if ( header == null ) {
            header = new SoapHeader(new QName(Constants.MESSAGE_SIGNATURE), signature, new JAXBDataBinding(String.class));
        }
        
        List<Header> headers = getSoapHeaders(message);
        if ( headers == null ) {
            headers = new ArrayList<Header>(0);
            message.put(Header.HEADER_LIST, headers);
        }
        headers.remove(header);
        headers.add(header);
    }
    
    private List<Header> getSoapHeaders(Message message) {
        return (List<Header>) message.get(Header.HEADER_LIST);
    }
    
    private Header findHeader(Message message) {
        List<Header> headers = getSoapHeaders(message);
        Header header = null;
        if ( headers != null ) {
            for ( Header h : headers ) {
                if ( h.getName().getLocalPart().equals(Constants.MESSAGE_SIGNATURE) ) {
                    header = h;
                    break;
                }
            }
        }
        return header;
    }
}
