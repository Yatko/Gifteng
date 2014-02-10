/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.fault;

import javax.xml.ws.WebFault;

/**
 *
 * @author gyuszi
 */
@SuppressWarnings("serial")
@WebFault(name = "RequestNotFoundError")
public class RequestNotFoundException extends Exception {
    
    public RequestNotFoundException(String message) {
        super(message);
    }

    public RequestNotFoundException(Long requestId) {
        super("Request with id = " + requestId + " not found!");
    }
}
