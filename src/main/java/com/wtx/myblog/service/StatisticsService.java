package com.wtx.myblog.service;

import com.wtx.myblog.utils.DataMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 26989
 * @date 2025/9/21
 * @description 统计服务接口
 */
public interface StatisticsService {


    DataMap getVisitorNumByPageName(String pageName, HttpServletRequest request);

    DataMap getStatisticsInfo(HttpServletRequest request);
}
