package com.wtx.myblog.service;

/**
 * @author 26989
 * @date 2025/9/21
 * @description
 */
public interface RedisService {
    /**
     * 判断key是否存在
     */
    Boolean hasKey(String key);
}
