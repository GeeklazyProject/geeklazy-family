package com.geeklazy.frame.filesystem.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "geeklazy.filesystem.oss"
)
@Data
public class OssConfigProperties {
    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
}
