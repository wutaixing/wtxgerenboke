package com.wtx.myblog.service;

import com.wtx.myblog.utils.DataMap;

/**
 * @author 26989
 * @date 2025/9/21
 * @description
 */
public interface FeedbackService {

    DataMap getAllFeedback(int rows, int pageNum);

    DataMap getAllPrivateWord();
}
