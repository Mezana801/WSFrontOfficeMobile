package vae.vae.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;

@Component
@ReadingConverter
public class DateTimeReadConverter implements Converter<Date, Time> {

    @Override
    public Time convert(Date date) {
        System.out.println("Reading Time Converter called");
        return new Time(date.getTime());
    }

}
