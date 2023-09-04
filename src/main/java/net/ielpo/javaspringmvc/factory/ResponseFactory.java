package net.ielpo.javaspringmvc.factory;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * @author Alberto Ielpo
 * @apiNote Response factory builder
 */
public class ResponseFactory {

    /**
     * Response builder
     * 
     * @param <T>
     * @param body
     * @return
     */
    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<T>(body, HttpStatusCode.valueOf(200));
    }

    /**
     * Response builder
     * 
     * @param <T>
     * @param body
     * @param httpCode
     * @return
     */
    public static <T> ResponseEntity<T> build(T body, int httpCode) {
        return new ResponseEntity<T>(body, HttpStatusCode.valueOf(httpCode));
    }
}
