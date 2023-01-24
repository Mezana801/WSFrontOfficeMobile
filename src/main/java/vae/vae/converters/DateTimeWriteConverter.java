package vae.vae.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;

@Component
@WritingConverter
public class DateTimeWriteConverter implements Converter<Time, Date> {

    @Override
    public java.util.Date convert(Time time) {
        Date resp = new Date(time.getTime());
        System.out.println("TIME = "+time+" et "+resp);
        return resp;
    }

}
