package vae.vae.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;


@Component
@ReadingConverter
public class UtilDateSqlDateReadConverter implements Converter<java.util.Date, java.sql.Date> {

    @Override
    public java.sql.Date convert(java.util.Date date) {
        System.out.println("Reading Converter called");
        return new java.sql.Date(date.getTime());
    }

}