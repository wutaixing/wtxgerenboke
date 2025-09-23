package com.wtx.myblog.mapper;

import com.wtx.myblog.model.FeedBack;
import com.wtx.myblog.model.PrivateWord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/21
 * @description
 */
@Mapper
@Repository
public interface FeedbackMappwer {
    List<FeedBack> getAllFeedback();

    List<PrivateWord> getAllPrivateWord();
}
