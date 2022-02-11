package com.example.events.eventsclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "events")
@Data
public class RepetitionRestClientProperties {
    private String url;
    private String basePath;
}
