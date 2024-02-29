package com.geeklazy.frame.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @Author heliuslee@live.cn
 * @Date 2018/4/27 0027 16:57
 * @Description
 */
public class EmptyUtils {
    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>
     * {@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof Number)
            return false;

        if (obj instanceof String)
            return obj.toString().length() == 0;

        if (obj.getClass().isArray())
            return Array.getLength(obj) == 0;

        if (obj instanceof Collection)
            return ((Collection<?>) obj).isEmpty();

        return obj instanceof Map && ((Map<?, ?>) obj).isEmpty();
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>
     * {@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 所传对象都不为空
     *
     * @param objects
     * @return
     */
    public static boolean allIsNotEmpty(Object... objects) {
        for (Object object : objects)
            if (isEmpty(object))
                return false;

        return true;
    }

    public static boolean isAbsoluteEmpty(Object obj) {
        if (isEmpty(obj))
            return true;

        if (obj instanceof String || obj.getClass().isArray() || obj instanceof Collection || obj instanceof Map || obj instanceof Date)
            return false;

        // 自定义对象对其字段遍历
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);

            try {
                if (!isAbsoluteEmpty(f.get(obj)))
                    // 字段不为空,则该对象不为空
                    return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


    public static boolean isAbsoluteNotEmpty(Object obj) {
        if (isEmpty(obj))
            return false;

        if (obj instanceof String || obj.getClass().isArray() || obj instanceof Collection || obj instanceof Map || obj instanceof Date)
            return true;

        // 自定义对象对其字段遍历
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);

            try {
                // 其中一个字段为空,则该对象不完全为空
                if (!isAbsoluteNotEmpty(f.get(obj))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
