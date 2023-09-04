package net.ielpo.javaspringmvc.provider;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.HttpMethod;

/**
 * @author Alberto Ielpo
 */
public class HttpServiceProviderOptions<T> {

    public HttpServiceProviderOptions() {
        this.headersMode = HeadersMode.APPEND;
    }

    public enum HeadersMode {
        APPEND, // append to default headers (DEFAULT behaviour)
        OVERRIDE // override default headers
    }

    public HttpMethod method;
    public Map<String, String> headers; // HttpHeaders, MediaType
    public HeadersMode headersMode;
    public Duration timeout;
    public T body;

    public String[] buildHeaders() {
        if (this.headers == null) {
            return new String[] {};
        }
        Set<Entry<String, String>> entries = this.headers.entrySet();
        String[] res = new String[entries.size() * 2];
        int ii = -1;
        for (Entry<String, String> entry : entries) {
            res[++ii] = entry.getKey();
            res[++ii] = entry.getValue();
        }

        return res;

    }

}
