package com.wtx.myblog.utils;

/**
 * @author 26989
 * @date 2025/9/6
 * @description
 */
public class StringCleanTrim {

    public static String cleanTrim(String username) {
        return new StringCleanTrim().StringCleanTrim(username);
    }
    private String StringCleanTrim(String username) {
        if (username == null) {
            return null;
        }

        // 1. 去除所有不可见字符（包括零宽空格等）
        String noInvisible = username.replaceAll("\\p{C}", "");

        // 2. 去除前后空格
        String trimmed = noInvisible.trim();

        // 3. 清理中间空格（多个连续空格 → 单个空格）
        String singleSpaced = trimmed.replaceAll("\\s+", " ");

        // 4. 去除特殊字符（只保留允许的字符）
        String cleaned = singleSpaced.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9_\\s]", "");

        return cleaned;
    }
}



