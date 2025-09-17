package com.wtx.myblog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/12
 * @description 返回前端草稿VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseVO {
    private static final long serialVersionUID = 1L;

    private int id;

    /**
     * 文章id
     */
    private long articleId;

    /**
     * 文章作者
     */
    private String author;

    /**
     * 文章原作者
     */
    private String originalAuthor;

    /**
     * 文章名
     */
    private String articleTitle;

    /**
     * 发布时间
     */
    private String publishDate;

    /**
     * 最后一次修改时间
     */
    private String updateDate;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章标签
     */
    private List<String> articleTags;

    /**
     * 文章类型
     */
    private String articleType;

    /**
     * 博客分类
     */
    private String articleCategories;


    /**
     * 原文链接
     * 转载：则是转载的链接
     * 原创：则是在本博客中的链接
     */
    private String articleUrl;

    /**
     * 文章摘要
     */
    private String articleTabloid;

    /**
     * 上一篇文章id
     */
    private long lastArticleId;

    /**
     * 下一篇文章id
     */
    private long nextArticleId;

    /**
     * 喜欢
     */
    private int likes = 0;

    /**
     * 发布状态 0:草稿 1:发布
     */
    private int releaseStatus;
}
