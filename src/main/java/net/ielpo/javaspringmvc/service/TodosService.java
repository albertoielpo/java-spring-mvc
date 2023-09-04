package net.ielpo.javaspringmvc.service;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.ielpo.javaspringmvc.dto.Todos;
import net.ielpo.javaspringmvc.provider.HttpServiceProvider;
import net.ielpo.javaspringmvc.provider.HttpServiceProviderOptions;

/**
 * @author Alberto Ielpo
 */
@Service
public class TodosService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private String baseUrl = "https://jsonplaceholder.typicode.com";

    @Autowired
    private HttpServiceProvider httpServiceProvider;

    @Autowired
    private ObjectMapper objectMapper;

    public TodosService() {
    }

    public Todos fetchData(Integer id) throws Exception {
        URI uri = new URI(String.format("%s/%s/%s", this.baseUrl, "todos", id));
        byte[] response = this.httpServiceProvider.request(uri, new HttpServiceProviderOptions<>() {
            {
                this.method = HttpMethod.GET;
            }
        });
        if (this.logger.isDebugEnabled()) {
            Object obj = objectMapper.readValue(response, Object.class);
            this.logger.debug("" + obj);
        }

        return objectMapper.readValue(response, Todos.class);
    }
}
