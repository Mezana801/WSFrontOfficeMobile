package vae.vae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import vae.vae.converters.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableMongoRepositories
@EnableScheduling
public class VaeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaeApplication.class, args);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new DateTimestampReadConverter());
        converters.add(new DateTimestampWriteConverter());
        converters.add(new DateTimeReadConverter());
        converters.add(new DateTimeWriteConverter());
        converters.add(new UtilDateSqlDateReadConverter());
        converters.add(new UtilDateSqlDateWriteConverter());

        return new MongoCustomConversions(converters);
    }
}

