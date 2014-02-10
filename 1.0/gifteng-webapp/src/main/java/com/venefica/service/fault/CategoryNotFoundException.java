package com.venefica.service.fault;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "CategoryNotFoundError")
public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
