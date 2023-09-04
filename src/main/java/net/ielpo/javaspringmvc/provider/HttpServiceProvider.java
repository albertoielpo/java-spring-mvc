package net.ielpo.javaspringmvc.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import net.ielpo.javaspringmvc.config.ConfigRepo;
import net.ielpo.javaspringmvc.config.Const;
import net.ielpo.javaspringmvc.exception.HttpServiceProviderException;
import net.ielpo.javaspringmvc.exception.Validate;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alberto Ielpo
 * @apiNote this class just call third party resource via http. No deserialize
 *          logic should be put in. The Service should decode the byte[]
 */
@Service
public class HttpServiceProvider {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConfigRepo configRepo;

    private String[] defaultHeaders = new String[] {
            HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE,
            HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE };

    /**
     * Send the request
     * 
     * @param request
     * @return
     * @throws Exception
     */
    private byte[] send(HttpRequest request) throws Exception {
        this.logger.info(String.format("%s: %s", request.method(), request.uri()));
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        HttpStatusCode hsc = HttpStatusCode.valueOf(response.statusCode());
        if (hsc.is2xxSuccessful()) {
            /** remote +2xx response... */
            return response.body();
        }

        /** error management */
        this.logger.error(
                response.body() == null ? Const.EMPTY_BODY : new String(response.body(), Charset.defaultCharset()));

        if (hsc.is4xxClientError()) {
            throw new HttpServiceProviderException(hsc, Const.REMOTE_CLIENT_ERROR, response.body(),
                    Charset.defaultCharset());
        }

        throw new HttpServiceProviderException(hsc, Const.REMOTE_SERVER_ERROR, response.body(),
                Charset.defaultCharset());
    }

    /**
     * Manage http headers
     * 
     * @param optionsHeaders
     * @param headersMode
     * @return
     */
    private String[] manageHeaders(String[] optionsHeaders, HttpServiceProviderOptions.HeadersMode headersMode) {
        switch (headersMode) {
            case OVERRIDE:
                return optionsHeaders;
            case APPEND:
            default:
                if (optionsHeaders.length == 0) {
                    return this.defaultHeaders;
                }

                String[] headers = Arrays.copyOf(this.defaultHeaders,
                        this.defaultHeaders.length + optionsHeaders.length);
                for (int ii = 0; ii < optionsHeaders.length; ii++) {
                    headers[ii + this.defaultHeaders.length] = optionsHeaders[ii];
                }

                return headers;
        }
    }

    /**
     * Perform http request
     * 
     * @param <T>
     * @param uri
     * @param body
     * @return
     * @throws Exception
     */
    public <T> byte[] request(URI uri, HttpServiceProviderOptions<T> options) throws Exception {

        Validate.or500(options.method != null, "Http method must be defined");

        BodyPublisher body;

        if (options.body == null) {
            body = HttpRequest.BodyPublishers.noBody();
        } else {
            body = HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(options.body));
        }

        Duration timeout = options.timeout == null ? Duration.ofSeconds(this.configRepo.getHttpServiceProviderTimeout())
                : options.timeout;
        String[] headers = this.manageHeaders(options.buildHeaders(), options.headersMode);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(timeout)
                .method(options.method.name(), body)
                .headers(headers)
                .build();

        return this.send(request);
    }

}
