package com.wtx.myblog.service.impl;

import com.wtx.myblog.mapper.TagMapper;
import com.wtx.myblog.model.Tags;
import com.wtx.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 26989
 * @date 2025/9/16
 * @description
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void insertTags(String[] newArticleTAgs, int tagSize) {
        // 插入之前，进行一个判断，去重标签
        for (String newArticleTAg : newArticleTAgs) {
            if(!tagMapper.findIsExistTagName(newArticleTAg)){
                Tags tag = new Tags(newArticleTAg, tagSize);
                tagMapper.saveTags(tag);
            }
        }

    }

    @Override
    public Integer getTagSizeByName(String tagName) {
        return tagMapper.getTagSizeByName(tagName);
    }
}
