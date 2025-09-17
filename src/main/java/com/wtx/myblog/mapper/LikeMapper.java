package com.wtx.myblog.mapper;

import com.wtx.myblog.model.ArticleLikesRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/17
 * @description
 */
@Mapper
@Repository
public interface LikeMapper {

    List<ArticleLikesRecord> getArticleThumbsUp();

    Integer getMsgIsNotReadNum();

    void readAllThumbsUp();
}
