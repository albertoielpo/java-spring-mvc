package net.ielpo.javaspringmvc.exception;

import java.nio.charset.Charset;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Alberto Ielpo
 * @apiNotes Throws this exception at service provider level (third party calls)
 */
public class HttpServiceProviderException extends HttpClientErrorException {

    public HttpServiceProviderException(HttpStatusCode statusCode, String statusText, byte[] body,
            Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);

    }

}
