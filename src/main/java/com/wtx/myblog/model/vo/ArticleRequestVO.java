package com.wtx.myblog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestVO {

    private Long id;
    private String articleTitle;
    private String articleContent;
    private String[] articleTagsValue;
    private String articleType;
    private String articleCategories;
    private Integer articleGrade;
    private String originalAuthor;
    private String articleUrl;
    private String articleHtmlContent;
    private String publishDate;
    private String author;
    private String updateDate;
    private Integer likes;

    private Integer lastArticleId;
    private Integer nextArticleId;

    // 发表状态 0: 草稿 1: 发表
    private Integer releaseStatus;

}
