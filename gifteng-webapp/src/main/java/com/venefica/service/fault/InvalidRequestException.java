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
@WebFault(name = "InvalidRequestError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class InvalidRequestException extends Exception {
    
    public InvalidRequestException(String message) {
        super(message);
    }
}
