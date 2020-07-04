package com.arc.db.jsd.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class LocalDateTimeConverter implements Converter<LocalDateTime> {
    @Override
    public Object j2d(LocalDateTime value) {
        return Timestamp.valueOf(value);
    }

    @Override
    public LocalDateTime d2j(Class<LocalDateTime> type, Object value) {
        return ((Timestamp)value).toLocalDateTime();
    }
}
