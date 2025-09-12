package com.wtx.myblog.service;

import com.wtx.myblog.model.vo.ArticleRequestVO;
import com.wtx.myblog.utils.DataMap;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
public interface ArticleService {
    DataMap insertArticle(ArticleRequestVO articleRequestVO);

    DataMap getAllArticle(Integer rows, Integer pageNum);

    DataMap deleteArticleByid(Integer id);

    DataMap getDraftArticle();
    DataMap getDraftArticle(Integer id);

    DataMap updateArticle(ArticleRequestVO articleRequestVO);

    DataMap canYouWrite();

    DataMap getUserPersonalInfo();

    DataMap getMyArticles(Integer rows, Integer pageNum);

    DataMap getArticleThumbsUp(Integer rows, Integer pageNum);

    DataMap markLikeAsRead(Integer id);

    DataMap markAllLikesAsRead(String author);
}
