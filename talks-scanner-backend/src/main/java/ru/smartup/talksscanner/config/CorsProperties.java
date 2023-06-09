package ru.smartup.talksscanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties for cors configure.
 * */
@ConstructorBinding
@ConfigurationProperties(prefix = "cors")
public class CorsProperties{
    private final String[] allowedOrigins;

    private final String[] allowedMethods;

    private final long maxAge;

    public CorsProperties(String[] allowedOrigins, String[] allowedMethods, long maxAge) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.maxAge = maxAge;
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public String[] getAllowedMethods() {
        return allowedMethods;
    }

    public long getMaxAge() {
        return maxAge;
    }

}