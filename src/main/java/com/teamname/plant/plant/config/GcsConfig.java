package com.teamname.plant.plant.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GcsConfig {

    @Bean
    public Storage storage() throws IOException {
        InputStream serviceAccountStream = getClass()
                .getClassLoader()
                .getResourceAsStream("gcp-key.json");

        assert serviceAccountStream != null;

        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(serviceAccountStream))
                .build()
                .getService();
    }
}
