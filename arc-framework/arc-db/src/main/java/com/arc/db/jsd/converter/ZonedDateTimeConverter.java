package com.arc.db.jsd.converter;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


public class ZonedDateTimeConverter implements Converter<ZonedDateTime> {
    @Override
    public Object j2d(ZonedDateTime value) {
        return Timestamp.from(value.toInstant());
    }

    @Override
    public ZonedDateTime d2j(Class<ZonedDateTime> type, Object value) {
        Timestamp ts = (Timestamp) value;
        return ZonedDateTime.ofInstant(ts.toInstant(), ZoneOffset.UTC);
    }
}
