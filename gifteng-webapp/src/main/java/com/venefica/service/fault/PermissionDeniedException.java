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
@WebFault(name = "PermissionDeniedException")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class PermissionDeniedException extends Exception {
    
    public PermissionDeniedException(String message) {
        super(message);
    }
}
