package com.wtx.myblog.service;

import com.wtx.myblog.model.Article;
import com.wtx.myblog.utils.DataMap;

import java.util.Map;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
public interface ArticleService {
    DataMap insertArticle(Article article);

    DataMap getArticleManagement(int rows, int pageNum);

    DataMap deleteArticle(String id);

    Article getArticleById(String id);

    DataMap getDraftArticle(Article article,String[] tagStr,Integer tagsSizeByName);

    DataMap updateArticle(Article article);

    DataMap getMyArticles(int rows, int pageNum);

    Map<String, String> findArticleTitleByArticleId(long articleId);

    DataMap getArticleByArticleId(long articleId, Integer userId);

    /**
     * 获取文章详情（包含用户点赞状态）
     * @param articleId 文章ID
     * @param userId 用户ID
     * @return 文章详情
     */
    DataMap getArticleDetailByArticleId(long articleId, Integer userId);
}
