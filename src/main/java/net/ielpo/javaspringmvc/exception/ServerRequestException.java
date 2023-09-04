package net.ielpo.javaspringmvc.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Alberto Ielpo
 * @apiNote Throws this exception if an input validation is failed.
 */
public class ServerRequestException extends HttpClientErrorException {

    /* default +500 http status code */
    public ServerRequestException(String statusText) {
        super(HttpStatusCode.valueOf(500), statusText);
    }

    public ServerRequestException(HttpStatusCode code, String statusText) {
        super(code, statusText);
    }
}
