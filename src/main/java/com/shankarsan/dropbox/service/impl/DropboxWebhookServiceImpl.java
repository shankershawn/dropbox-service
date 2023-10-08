package com.shankarsan.dropbox.service.impl;

import com.shankarsan.dropbox.service.DropboxService;
import com.shankarsan.dropbox.service.DropboxWebhookService;
import com.shankarsan.dropbox.service.constants.CommonConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DropboxWebhookServiceImpl implements DropboxWebhookService
{

    private final CacheManager cacheManager;
    private final DropboxService dropboxService;

    @Override
    public File refreshAvailabilityFileData() {
        log.debug("Refreshing file data from Dropbox");
        File availabilityFileData = dropboxService.downloadFile(CommonConstants.SEAT_AVAILABILITY_DAT);
        Optional.ofNullable(cacheManager)
                .map(e -> e.getCache(CommonConstants.DROPBOX_AVAILABILITY_FILE_CACHE))
                .ifPresent(cache -> cache.put(CommonConstants.SEAT_AVAILABILITY_FILE_DATA, availabilityFileData));
        return availabilityFileData;
    }
}
