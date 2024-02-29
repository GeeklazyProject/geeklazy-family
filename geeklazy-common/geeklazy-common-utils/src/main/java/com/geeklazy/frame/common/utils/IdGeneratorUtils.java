package com.geeklazy.frame.common.utils;

import java.util.UUID;

/**
 * @Author geeklazy@163.com
 * @DATE 2023/3/28 13:34
 * @Description
 */
public class IdGeneratorUtils {
    public static String generateUUIDWithoutSplit() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
