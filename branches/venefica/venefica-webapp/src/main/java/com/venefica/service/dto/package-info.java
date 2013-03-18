@XmlSchema(namespace = Namespace.SERVICE, attributeFormDefault = XmlNsForm.QUALIFIED, elementFormDefault = XmlNsForm.QUALIFIED)
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class),
    @XmlJavaTypeAdapter(value = CalendarAdapter.class, type = Calendar.class),
})
package com.venefica.service.dto;

import com.venefica.service.Namespace;
import com.venefica.service.dto.serialization.CalendarAdapter;
import com.venefica.service.dto.serialization.DateAdapter;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
