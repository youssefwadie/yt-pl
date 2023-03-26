package com.github.youssefwadie.ytpl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "youtube")
public class YoutubeApiProperties {
    private String apiKey;
    private String apiBaseUrl;

}
