package com.venefica.service.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class DtoBase {

    @SuppressWarnings("unused")
    //NOTE: why was this namespace set?
    //@XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    @XmlAttribute(name = "type")
    private String type;

    public DtoBase() {
        type = getClass().getSimpleName();
    }
}
