package vae.vae.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;


@Component
@WritingConverter
public class UtilDateSqlDateWriteConverter implements Converter<java.sql.Date, java.util.Date> {

    @Override
    public java.util.Date convert(java.sql.Date date) {
        System.out.println("Reading Converter called");
        return new java.util.Date(date.getTime());
    }

}
