package com.github.youssefwadie.ytpl.config;


import com.github.youssefwadie.ytpl.client.YoutubeApiClient;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class YoutubeApiClientConfig {

    @Bean
    @Autowired
    public RestTemplate restTemplate(final UriBuilderFactory uriBuilderFactory) {
        return new RestTemplateBuilder()
                .uriTemplateHandler(uriBuilderFactory)
                .build();
    }

    @Bean
    @Autowired
    public YoutubeApiClient youtubeApiClient(final RestTemplate restTemplate, final UriBuilderFactory uriBuilderFactory) {
        return new YoutubeApiClient(restTemplate, uriBuilderFactory);
    }

    @Bean
    @Autowired
    public UriBuilderFactory uriBuilderFactory(final YoutubeApiProperties properties) {
        System.out.println("apiKey " + properties.getApiKey());
        val authorizedUriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(properties.getApiBaseUrl())
                .queryParam("key", properties.getApiKey());
        return new DefaultUriBuilderFactory(authorizedUriComponentsBuilder);
    }

}
