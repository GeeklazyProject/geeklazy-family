package com.geeklazy.frame.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @Author geeklazy@163.com
 * @Date 2023/11/24 14:30
 * @Description
 */
public class SysEnvUtils {
    private SysEnvUtils() {
    }

    public static String getSys(String key) {
        return System.getProperty(key);
    }

    public static String getSys(String key, String defVal) {
        String sys = getSys(key);
        return EmptyUtils.isEmpty(sys) ? defVal : sys;
    }

    public static String getEnv(String key) {
        return System.getenv(key);
    }

    public static String getEnv(String key, String defVal) {
        String env = getEnv(key);
        return EmptyUtils.isEmpty(env) ? defVal : env;
    }

    public static String getSysOrEnv(String key) {
        String sys = getSys(key);
        if (EmptyUtils.isEmpty(sys)) {
            return getEnv(key);
        }
        return sys;
    }

    public static String getSysOrEnv(String key, String defVal) {
        String value = getSysOrEnv(key);
        return EmptyUtils.isEmpty(value) ? defVal : value;
    }

    public static Map<String, String> toMap(Properties props) {
        return props.stringPropertyNames().stream().collect(Collectors.toMap(k -> k, props::getProperty));
    }
}
