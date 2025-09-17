package com.wtx.myblog.service;

import com.wtx.myblog.utils.DataMap;

/**
 * @author 26989
 * @date 2025/9/17
 * @description
 */
public interface LikeService {
    DataMap getArticleThumbsUp(int rows, int pageNum);

    DataMap readAllThumbsUp();
}
