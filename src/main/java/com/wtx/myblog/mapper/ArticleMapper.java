package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Mapper
@Repository
public interface ArticleMapper {
    void saveArticle(Article article);


    int inserttags(@Param("tagName") String tagName,@Param("tagSize") Integer tagSize);


    /**
     * 查找早于当前文章的最新文章
     */
    Long findLatestArticleBefore(Long currentArticleId);

    /**
     * 查找晚于当前文章的最早文章
     */
    Long findEarliestArticleAfter(Long currentArticleId);

    /**
     * 更新文章的上下关系
     */
    void updateArticleRelations(@Param("currentId") Long currentId,
                                @Param("lastId") Long lastId,
                                @Param("nextId") Long nextId);

    void updateLastArticleNextPointer(@Param("lastArticleId") Long lastArticleId,@Param("currentArticleId") Long currentArticleId);

    void updateNextArticleLastPointer(@Param("nextArticleId") Long nextArticleId,@Param("currentArticleId") Long currentArticleId);

    List<Article> getArticleManagement();

    Article getArticleById(String id);

    void updateLastNextId(@Param("LastOrNextStr") String LastOrNextStr,@Param("updateId") long updateId,@Param("articleId") long articleId);

    void deleteArticle(long articleId);

    Article getArticleByIntId(int id);

    void updateArticleById(Article article);

    String getArticleByTitleArticleId(long articleId);

    String getArticleAuthorByArticleId(long articleId);

    Article getArticleByArticleId(long articleId);

    int getarticleNum();

    /**
     * 更新文章点赞数
     * @param articleId 文章ID
     * @param likes 点赞数
     * @return 影响行数
     */
    int updateArticleLikes(@Param("articleId") Long articleId, @Param("likes") Integer likes);

    /**
     * 增加文章点赞数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementArticleLikes(@Param("articleId") Long articleId);

    /**
     * 减少文章点赞数
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementArticleLikes(@Param("articleId") Long articleId);

    int getArticleLikesByArticleId(Long articleId);

    long countarticleNum();

    void updateLikeByArticleId(String articleId);

    int queryLiksByarticleId(String articleId);

    List<Article> getArticleByArticleCategories(@Param("articleCategories") String articleCategories);

    int findArticleNumByCategoryName(String categoriesName);

    List<Article> getAllArticleByArticleCategories();
}
