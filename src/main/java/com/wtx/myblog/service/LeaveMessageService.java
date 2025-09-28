package com.wtx.myblog.service;

import com.wtx.myblog.utils.DataMap;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
public interface LeaveMessageService {
    DataMap publishLeaveMessage(String username,String leaveMessageContent, String pageName);

    DataMap getPageLeaveMessage(String pageName,String username);
}
