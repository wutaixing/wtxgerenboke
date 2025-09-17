package com.wtx.myblog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 26989
 * @date 2025/9/12
 * @description 返回前端的已读文章数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsReadResponseVO {
    private int id;
    private int articleId; //文章id
    private int likerId;
    private String praisePeople; //点赞的人
    private String articleTitle; //文章标题
    private String likeDate;
    private int isRead;


//    TimeUtil timeUtil = new TimeUtil();
//                String formatDateForSix = timeUtil.getFormatDateForSix();
//                articleRequestVO.setPublishDate(formatDateForSix);
}
