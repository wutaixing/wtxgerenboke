package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Role;
import com.wtx.myblog.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/6
 * @description
 */
@Mapper
@Repository
public interface TagsMapper {
    int inserttags(@Param("tagName") String tagName,@Param("tagSize") Integer tagSize);

    int findTagsByArticleTitle(@Param("tagName") String tagName);

    int updatetags(@Param("tagName") String tagName,@Param("tagSize") Integer tagSize);

    int deleteTagsByArticleTitle(@Param("tagName") String articleTitle);

    int getTagsByArticleTitle(@Param("tagName") String tagName);
}
