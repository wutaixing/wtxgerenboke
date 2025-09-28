package com.wtx.myblog.mapper;

import com.wtx.myblog.model.ArticleLikesRecord;
import com.wtx.myblog.model.FriendLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    List<FriendLink> getFriendLink();

    void addFriendLink(FriendLink friendLink);

    int findIsExistBylogger(String blogger);

    void updateFriendLink(@Param("friendLink") FriendLink friendLink, @Param("id") String id);

    void deleteFriendLink(String id);

    ArticleLikesRecord isLiked(@Param("articleId") Long articleId, @Param("likerId") int likerId);

    void insertArticleLikesRecord(ArticleLikesRecord articleLikesRecord);
}
