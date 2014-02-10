/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * Thrown when the invitation is not valid:
 * - is expired
 * - the maximum available use is reached
 * 
 * @author gyuszi
 */
@WebFault(name = "InvalidInvitationException")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class InvalidInvitationException extends Exception {
    
    public InvalidInvitationException(String message) {
        super(message);
    }
}
