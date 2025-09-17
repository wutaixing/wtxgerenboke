package com.wtx.myblog.service;

import com.wtx.myblog.model.Article;
import com.wtx.myblog.utils.DataMap;

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
}
