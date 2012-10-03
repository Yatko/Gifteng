package com.venefica.service.dto.serialization;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Used to serialize (deserialize) a date object within a soap message.
 * 
 * @author Sviatoslav Grebenchukov
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date date) throws Exception {
		return date != null ? new Long(date.getTime()).toString() : null;
	}

	@Override
	public Date unmarshal(String ticks) throws Exception {
		return ticks != null && !ticks.equals("") ? new Date(Long.parseLong(ticks)) : null;
	}

}
