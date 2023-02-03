package vae.vae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import vae.vae.converters.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableMongoRepositories
@EnableScheduling
public class VaeApplication {

    public static void main(String[] args) {

        SpringApplication.run(VaeApplication.class, args);
        testWS();
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

    public static void testWS(){
        String url = "https://onesignal.com/api/v1/notifications";
        String json = "{\"app_id\":\"51533bda-7d07-4a70-91e6-dcf1b90fcb9a\",\"included_segments\":[\"Subscribed Users\"],\"contents\":{\"en\":\"English or Any Language Message\"},\"name\":\"INTERNAL_CAMPAIGN_NAME\"}";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    }
}

