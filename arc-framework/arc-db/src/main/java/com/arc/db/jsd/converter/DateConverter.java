package com.arc.db.jsd.converter;

import java.sql.Timestamp;
import java.util.Date;


public class DateConverter implements Converter<Date> {
    @Override
    public Object j2d(Date value) {
        return new Timestamp(value.getTime());
    }

    @Override
    public Date d2j(Class<Date> type, Object value) {
        return (Date)value;
    }
}
