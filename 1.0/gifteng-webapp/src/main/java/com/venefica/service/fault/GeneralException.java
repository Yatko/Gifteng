package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebFault;

@WebFault(name = "GeneralError")
//@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class GeneralException extends Exception {

    public static final int GENERAL_ERROR = 0;
    
    @SuppressWarnings("unused")
    private int errorCode;
    
    public GeneralException(String message) {
        super(message);
    }
    
    public GeneralException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
