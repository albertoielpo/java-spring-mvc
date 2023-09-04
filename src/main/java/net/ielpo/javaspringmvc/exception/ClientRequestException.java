package net.ielpo.javaspringmvc.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Alberto Ielpo
 * @apiNote Throws this exception if an input validation is failed.
 */
public class ClientRequestException extends HttpClientErrorException {

    /* default +400 http status code */
    public ClientRequestException(String statusText) {
        super(HttpStatusCode.valueOf(400), statusText);
    }

    public ClientRequestException(HttpStatusCode code, String statusText) {
        super(code, statusText);
    }
}
