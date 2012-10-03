@XmlSchema(namespace = Namespace.SERVICE, attributeFormDefault = XmlNsForm.QUALIFIED, elementFormDefault = XmlNsForm.QUALIFIED)
@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
package com.venefica.service.dto;

import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlNsForm;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.venefica.service.Namespace;
import com.venefica.service.dto.serialization.DateAdapter;

import java.util.Date;


