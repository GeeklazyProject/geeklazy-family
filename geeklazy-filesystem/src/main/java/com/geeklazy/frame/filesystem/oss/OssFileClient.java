package com.geeklazy.frame.filesystem.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import com.geeklazy.frame.filesystem.FileClient;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class OssFileClient implements FileClient {
    private final OSS oss;
    private final OssConfigProperties properties;

    private final static Pattern FILE_PATH_PATTERN = Pattern.compile("[^/]+");

    public OssFileClient(OSS oss, OssConfigProperties properties) {
        this.oss = oss;
        this.properties = properties;
    }

    @Override
    public String upload(String filePath, String fileName, String fileType, InputStream inputStream) {
        Matcher matcher = FILE_PATH_PATTERN.matcher(filePath);
        if (matcher.find()) {
            String bucketName = matcher.group();
            String actualFilePath = filePath.replace("/" + bucketName + "/", "");
            String fullFileName = actualFilePath + "/" + fileName + "." + fileType;
            PutObjectResult result = oss.putObject(bucketName, fullFileName, inputStream);
            return "https://" + bucketName + "." + properties.getEndPoint() + "/" + fullFileName;
        } else {
            log.error(String.format("path: %s bucket not found", filePath));
            return null;
        }
    }
}
