package vae.vae.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@WritingConverter
public class DateTimestampWriteConverter implements Converter<Timestamp, Date> {

    @Override
    public Date convert(Timestamp zonedDateTime) {
        System.out.println("Writing Converter called");
        return Date.from(zonedDateTime.toInstant());
    }

}