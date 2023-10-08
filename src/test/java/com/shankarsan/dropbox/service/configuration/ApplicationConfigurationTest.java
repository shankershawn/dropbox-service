package com.shankarsan.dropbox.service.configuration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ApplicationConfigurationTest {

    public static final String SOME_KEY = "some key";
    public static final String SOME_SECRET = "some secret";
    public static final String SOME_URL = "some url";
    @InjectMocks
    private ApplicationConfiguration applicationConfiguration;

    private static final Map<String, String> secretsMap = new HashMap<>();

    private static final Map<String, String> urlMap = new HashMap<>();

    @BeforeAll
    public static void setup() {
        log.info("Inside setup");
        secretsMap.put(SOME_KEY, SOME_SECRET);
        urlMap.put(SOME_KEY, SOME_URL);
    }

    @Test
    void test1() {
        log.info("Inside test1");
        applicationConfiguration.setSecretsMap(secretsMap);
        applicationConfiguration.setUrlMap(urlMap);
        assertEquals(SOME_SECRET, applicationConfiguration.getSecret(SOME_KEY));
        assertEquals(SOME_URL, applicationConfiguration.getUrl(SOME_KEY));
    }
}
