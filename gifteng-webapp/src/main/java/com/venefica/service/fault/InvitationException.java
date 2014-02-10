/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 *
 * @author gyuszi
 */
@SuppressWarnings("serial")
@WebFault(name = "InvitationError")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvitationException extends Exception {
    
    @SuppressWarnings("unused")
    private int errorCode;
    
    public InvitationException(String message) {
        super(message);
    }
    
    public InvitationException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
