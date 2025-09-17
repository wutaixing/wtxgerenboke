package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author 26989
 * @date 2025/9/16
 * @description
 */
@Mapper
@Repository
public interface TagMapper {
    void saveTags(Tags tag);
    Boolean findIsExistTagName(String tagName);

    Integer getTagSizeByName(String tagName);
}
