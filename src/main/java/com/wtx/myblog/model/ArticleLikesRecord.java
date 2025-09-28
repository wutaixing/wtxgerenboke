package com.wtx.myblog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 26989
 * @date 2025/9/22
 * @description 文章点赞记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikesRecord {
    
    private Integer id;
    private Long articleId;
    private Integer likerId;
    private String likeDate;
    private Integer isRead;

    public ArticleLikesRecord(Long articleId, Integer likerId, String likeDate, Integer isRead) {
        this.articleId = articleId;
        this.likerId = likerId;
        this.likeDate = likeDate;
        this.isRead = isRead;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Long getArticleId() {
        return articleId;
    }
    
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    
    public Integer getLikerId() {
        return likerId;
    }
    
    public void setLikerId(Integer likerId) {
        this.likerId = likerId;
    }
    
    public String getLikeDate() {
        return likeDate;
    }
    
    public void setLikeDate(String likeDate) {
        this.likeDate = likeDate;
    }
    
    public Integer getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
    
    @Override
    public String toString() {
        return "ArticleLikesRecord{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", likerId=" + likerId +
                ", likeDate='" + likeDate + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}