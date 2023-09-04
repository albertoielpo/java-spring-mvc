package net.ielpo.javaspringmvc.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.ielpo.javaspringmvc.config.ConfigRepo;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Alberto Ielpo
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String DEFAULT_ERROR_MESSAGE = "Internal server error";

    @Autowired
    private ConfigRepo configRepo;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {

        /** log everything */
        this.logger.error("handle:", ex);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        HttpStatusCode httpStatus = null;
        String error = null;
        String stack = null;

        if (ex instanceof HttpServiceProviderException) {
            /** remote call exception .. */
            /** httpStatus is always 500 (default as is put in the stack) */
            httpStatus = HttpStatusCode.valueOf(500);
            error = ((HttpServiceProviderException) ex).getStatusText();

            /** stack for debug only */
            if (this.configRepo.getGlobalExceptionStackMessage()) {
                stack = String.format("Remote http code: %s - Payload: %s",
                        ((HttpServiceProviderException) ex).getStatusCode(),
                        ((HttpServiceProviderException) ex).getResponseBodyAsString());
            }

        } else if (ex instanceof ClientRequestException
                || ex instanceof ServerRequestException) {

            /**
             * ClientRequestException and ServerRequestException extends
             * HttpClientErrorException but are managed by the application.
             * Propagate the same status code
             */
            httpStatus = ((HttpClientErrorException) ex).getStatusCode();
            error = ((HttpClientErrorException) ex).getStatusText();
        } else {
            /* default */
            httpStatus = HttpStatusCode.valueOf(500);
            error = this.DEFAULT_ERROR_MESSAGE;
        }

        /*
         * spring default body
         * {
         * "timestamp": 1693571014184,
         * "status": 404,
         * "error": "Not Found",
         * "path": "/"
         * }
         */
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", httpStatus.value());
        body.put("error", error);
        body.put("path", this.httpServletRequest.getRequestURI());
        if (stack != null) {
            body.put("stack", stack);
        }

        return handleExceptionInternal(ex, body, httpHeaders, httpStatus, request);
    }
}