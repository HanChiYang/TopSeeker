package com.topseeker.shop.sale.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateToTimestampConverter implements Converter<java.util.Date, java.sql.Timestamp> {

    @Override
    public java.sql.Timestamp convert(java.util.Date source) {
        if (source == null) {
            return null;
        }
        return new java.sql.Timestamp(source.getTime());
    }
    
    

}
