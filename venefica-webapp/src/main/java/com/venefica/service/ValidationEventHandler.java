/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

/**
 * Not using yet. Will be enabled with the validation.
 * 
 * @author gyuszi
 */
public class ValidationEventHandler extends DefaultValidationEventHandler {

    @Override
    public boolean handleEvent(ValidationEvent event) {
        if (event.getSeverity() == ValidationEvent.WARNING) {
            return super.handleEvent(event);
        } else {
            throw new RuntimeException(event.getMessage() + " [line:" + event.getLocator().getLineNumber() + "]");
        }
    }
}
