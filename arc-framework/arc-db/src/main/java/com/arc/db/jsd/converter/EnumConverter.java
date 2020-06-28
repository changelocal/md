package com.arc.db.jsd.converter;


import com.arc.util.lang.EnumValueSupport;
import com.arc.util.lang.Enums;

public class EnumConverter<T extends Enum & EnumValueSupport> implements Converter<T> {
    @Override
    public Object j2d(T value) {
        return value.value();
    }

    @Override
    public T d2j(Class<T> type, Object value) {
        return Enums.valueOf(type, (int)value);
    }
}
