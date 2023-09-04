package net.ielpo.javaspringmvc.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ielpo.javaspringmvc.dto.Todos;
import net.ielpo.javaspringmvc.exception.Validate;
import net.ielpo.javaspringmvc.factory.ResponseFactory;
import net.ielpo.javaspringmvc.service.TodosService;

/**
 * @author Alberto Ielpo
 */
@Component
@RestController
@RequestMapping(value = "remote-test", produces = MediaType.APPLICATION_JSON_VALUE)
public class HelloworldController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TodosService todosService;

    @GetMapping(value = "")
    public ResponseEntity<Todos> remote(@RequestParam(value = "id") Optional<Integer> id) throws Exception {

        /** prechecks */
        Validate.or400(id.isPresent(), "Id is mandatory");

        /** This is a test of +400 managed error */
        Validate.or400(id.get() > 0 && id.get() <= 205, "Id must be between 0 and 205");

        /** This is a test of +500 managed error: param id == 201 throws +500 error */
        Validate.or500(!id.get().equals(201), "id cannot be 201");

        /** This is an unmanaged error: param id == 202 throws +500 error: */
        Assert.isTrue(!id.get().equals(202), "id cannot be 202");

        /** id == 203, 204, 205 should throws HttpServiceProviderException */

        this.logger.info("Calling todo fetch data");
        Todos result = this.todosService.fetchData(id.get());
        this.logger.info("Data retrieved");
        return ResponseFactory.ok(result);
    }

}
