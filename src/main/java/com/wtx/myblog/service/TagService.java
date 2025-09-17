package com.wtx.myblog.service;

import com.wtx.myblog.model.Tags;

/**
 * @author 26989
 * @date 2025/9/16
 * @description 标签
 */
public interface TagService {
    void insertTags(String[] newArticleTAgs, int tagSize);

    Integer getTagSizeByName(String tagStr);
}
