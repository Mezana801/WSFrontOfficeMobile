package vae.vae.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@ReadingConverter
public class DateTimestampReadConverter implements Converter<Date, Timestamp> {

    @Override
    public Timestamp convert(Date date) {
        System.out.println("Reading Converter called");
        return new Timestamp(date.getTime());
    }

}