package com.geeklazy.frame.filesystem.autoconfig;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.geeklazy.frame.filesystem.FileClient;
import com.geeklazy.frame.filesystem.oss.OssConfigProperties;
import com.geeklazy.frame.filesystem.oss.OssFileClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OssConfigProperties.class})
public class FileSystemAutoConfiguration {
    private final OssConfigProperties ossConfigProperties;

    public FileSystemAutoConfiguration(OssConfigProperties ossConfigProperties) {
        this.ossConfigProperties = ossConfigProperties;
    }

    @Bean
    public OSS oss() {
        return new OSSClientBuilder().build(ossConfigProperties.getEndPoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
    }

    @Bean
    public FileClient fileClient() {
        return new OssFileClient(oss(), ossConfigProperties);
    }
}
