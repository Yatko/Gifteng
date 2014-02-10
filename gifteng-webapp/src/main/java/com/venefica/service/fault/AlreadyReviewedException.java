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
@WebFault(name = "AlreadyReviewedError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class AlreadyReviewedException extends Exception {
    
    public AlreadyReviewedException(String message) {
        super(message);
    }
}
