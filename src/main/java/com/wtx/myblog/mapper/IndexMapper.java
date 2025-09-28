package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Comment;
import com.wtx.myblog.model.LeaveMessage;
import com.wtx.myblog.model.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/23
 * @description
 */
@Mapper
@Repository
public interface IndexMapper {
    long getarticleNum();

    long gettagsNum();

    long getmessageNum();

    long getcommentNum();

    List<Comment> getAllCommentOrderByDate();

    List<LeaveMessage> getAllLeaveMessageOrderByDate();

    List<Tags> getAllTagsCloud();

    long getCommentNumNotReadNum();

    long getMessageNotReadNum();
}
