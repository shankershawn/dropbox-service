package com.shankarsan.dropbox.service.impl;

import com.shankarsan.dropbox.service.DropboxService;
import com.shankarsan.dropbox.service.configuration.ApplicationConfiguration;
import com.shankarsan.dropbox.service.constants.CommonConstants;
import com.shankarsan.dropbox.service.dto.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DropboxServiceImpl implements DropboxService {

    @Qualifier("dropboxShortLivedTokenRestTemplate")
    private final RestTemplate dropboxShortLivedTokenRestTemplate;

    @Qualifier("dropboxDownloadRestTemplate")
    private final RestTemplate dropboxDownloadRestTemplate;

    private final ApplicationConfiguration applicationConfiguration;

    @Override
    public final File downloadFile(String path) {
        ResponseEntity<File> downloadResponseEntity;
        String shortLivedToken = Optional.ofNullable(getRefreshToken())
                .map(RefreshTokenDto::getAccessToken)
                .orElseThrow(() -> new IllegalStateException("Unable to fetch short-lived token"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + shortLivedToken);
        httpHeaders.add("Dropbox-API-Arg",
                "{\"path\": \"" + path + "\"}");
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        log.debug("Downloading file from dropbox");
        downloadResponseEntity = dropboxDownloadRestTemplate
                .exchange("/2/files/download", HttpMethod.POST,
                        httpEntity, File.class, Collections.emptyMap());
        return downloadResponseEntity.getBody();

    }

    private RefreshTokenDto getRefreshToken() {
        log.debug("Fetching refreshToken");
        String resourcePath = String.format("/oauth2/token?grant_type=refresh_token&refresh_token=%s",
                applicationConfiguration.getSecret(CommonConstants.DROPBOX_REFRESH_TOKEN));
        ResponseEntity<RefreshTokenDto> refreshTokenResponseEntity = dropboxShortLivedTokenRestTemplate
                .exchange(resourcePath,
                        HttpMethod.POST, null, RefreshTokenDto.class, Collections.emptyMap());
        return refreshTokenResponseEntity.getBody();
    }
}
