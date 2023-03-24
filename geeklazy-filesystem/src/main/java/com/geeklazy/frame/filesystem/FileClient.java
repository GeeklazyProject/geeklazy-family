package com.geeklazy.frame.filesystem;

import java.io.InputStream;

public interface FileClient {
    String upload(String filePath, String fileName, String fileType, InputStream inputStream);
}
