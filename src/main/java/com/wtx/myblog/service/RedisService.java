package com.wtx.myblog.service;

import com.wtx.myblog.utils.DataMap;

import javax.servlet.http.HttpServletRequest;

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
