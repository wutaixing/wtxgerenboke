package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.service.StatisticsService;
import com.wtx.myblog.service.impl.RedisServiceImpl;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import com.wtx.myblog.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 26989
 * @date 2025/9/21
 * @description 统计模块控制器
 */
@RestController
@Slf4j
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private RedisServiceImpl redisService;

    /**
     * @description: //TODO 记录网站访问量以及访客量
     * @param: request
     * @param: pageName
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/23 11:03
    */
    @GetMapping("/getVisitorNumByPageName")
    public String getVisitorNumByPageName(HttpServletRequest  request ,String pageName){
        try {
            //获得页面名称，页面名称默认为“visitorVolume”
            int index = pageName.indexOf("/");
            if(index == -1){
                pageName = "visitorVolume";
            }
            DataMap data = statisticsService.getVisitorNumByPageName(pageName,request);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【StatisticsController】getVisitorNumByPageName！", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //仪表盘统计模块
     * @param: request
     * @param: pageName
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/23 16:28
    */
    @GetMapping("/getStatisticsInfo")
    public String getStatisticsInfo(HttpServletRequest  request){
        try {
            DataMap data = statisticsService.getStatisticsInfo(request);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【StatisticsController】getStatisticsInfo！", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
