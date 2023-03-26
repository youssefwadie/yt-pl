package com.github.youssefwadie.ytpl;

import com.github.youssefwadie.ytpl.config.YoutubeApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(YoutubeApiProperties.class)
public class YtPlApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtPlApplication.class, args);
	}

}
