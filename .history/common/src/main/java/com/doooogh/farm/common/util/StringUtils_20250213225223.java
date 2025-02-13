package com.doooogh.farm.common.util;

public class StringUtils {

    /**
     * 根据指定字符分割字符串，并获取指定下标的字符串。
     *
     * @param input     要分割的字符串
     * @param delimiter 分隔符
     * @param index     要获取的字符串下标
     * @return 指定下标的字符串，如果下标超出范围，返回 null
     */
    public static String getStringAtIndex(String input, String delimiter, int index) {
        if (input == null || delimiter == null || index < 0) {
            return null;
        }

        String[] parts = input.split(delimiter);
        if (index >= parts.length) {
            return null;
        }

        return parts[index];
    }

    /**
     * 根据指定字符分割字符串，并获取第一个字符串。
     *
     * @param input     要分割的字符串
     * @param delimiter 分隔符
     * @return 第一个字符串，如果字符串为空或分隔符为空，返回 null
     */
    public static String getFirstString(String input, String delimiter) {
        return getStringAtIndex(input, delimiter, 0);
    }
} 