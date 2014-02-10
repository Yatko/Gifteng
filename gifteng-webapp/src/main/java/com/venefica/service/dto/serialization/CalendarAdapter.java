package com.venefica.service.dto.serialization;

import java.util.Calendar;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Used to serialize (deserialize) a calendar object within a soap message.
 *
 * @author Sviatoslav Grebenchukov
 */
public class CalendarAdapter extends XmlAdapter<String, Calendar> {

    @Override
    public String marshal(Calendar cal) throws Exception {
        return cal != null ? new Long(cal.getTimeInMillis()).toString() : null;
    }

    @Override
    public Calendar unmarshal(String ticks) throws Exception {
        if ( ticks != null && !ticks.equals("") ) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(ticks));
            return cal;
        }
        return null;
    }
}
