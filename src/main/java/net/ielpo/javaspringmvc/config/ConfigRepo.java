package net.ielpo.javaspringmvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Alberto Ielpo
 */
@Component
public class ConfigRepo {

    @Value("${app.http-service-provider-timeout:5}")
    private Integer httpServiceProviderTimeout;

    @Value("${app.global-exception-stack-message:false}")
    private Boolean globalExceptionStackMessage;

    public Integer getHttpServiceProviderTimeout() {
        return httpServiceProviderTimeout;
    }

    public Boolean getGlobalExceptionStackMessage() {
        return globalExceptionStackMessage;
    }

}
