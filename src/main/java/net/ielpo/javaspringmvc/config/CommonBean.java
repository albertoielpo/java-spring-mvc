package net.ielpo.javaspringmvc.config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alberto Ielpo
 */
@Configuration
public class CommonBean {

    @Bean
    ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }
}
