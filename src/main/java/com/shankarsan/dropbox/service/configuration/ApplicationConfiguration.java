package com.shankarsan.dropbox.service.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@Configuration("dropbox-appconfig")
@ConfigurationProperties(prefix = "app")
@RequiredArgsConstructor
@Data
@RefreshScope
public class ApplicationConfiguration {

    private Map<String, String> secretsMap;

    private Map<String, String> urlMap;

    public String getUrl(String key) {
        return extractFromMap(this.urlMap, key);
    }

    public String getSecret(String key) {
        return extractFromMap(this.secretsMap, key);
    }

    private String extractFromMap(Map<String, String> mapToExtract, String key) {
        return Optional.ofNullable(mapToExtract).map(e -> e.get(key)).orElse(null);
    }
}
