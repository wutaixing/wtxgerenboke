package com.wtx.myblog.config;

import com.wtx.myblog.mapper.VisitorMapper;
import com.wtx.myblog.service.impl.HashRedisServiceImpl;
import com.wtx.myblog.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.plaf.SeparatorUI;
import java.util.LinkedHashMap;

/**
 * @author 26989
 * @date 2025/9/23
 * @description 用定时任务统计数据到redis中
 */
@Component
public class Scheduledtask {

    @Autowired
    private VisitorMapper visitorMapper;
    @Autowired
    private HashRedisServiceImpl hashRedisServiceImpl;

    /**
     * @description: //定时任务 统计数据到redis中
     * @param: https://cron.qqe2.com/  生成工具
     * @return: void
     * @author 26989
     * @date: 2025/9/23 17:04
    */
    @Scheduled(cron = "0 0 0 * * ?") // 0 0 0 * * ?  每天0点0分0秒执行
    public void statisticsVisitorNum() {
        //查询出旧总访问量
        long oldTotalVisitor = visitorMapper.getTotalVisitor();
        //查询当前最新的总访问量
        Long newTotalVisitor = Long.valueOf(hashRedisServiceImpl.get(StringUtil.VISITOR, "totalVisitor").toString());
        //作差获取到昨日访问量
        long yesterdayVisitor = newTotalVisitor - oldTotalVisitor;
        if(hashRedisServiceImpl.hasHashKey(StringUtil.VISITOR,"yesterdayVisitor")){
            hashRedisServiceImpl.put(StringUtil.VISITOR,"yesterdayVisitor",yesterdayVisitor);
        }else {
            hashRedisServiceImpl.put(StringUtil.VISITOR,"yesterdayVisitor",oldTotalVisitor);
        }
        //将redis中的所有访客记录更新到数据库中
        LinkedHashMap map = (LinkedHashMap)hashRedisServiceImpl.getAllFieldAndValue(StringUtil.VISITOR);
        String pageName;
        for(Object o : map.keySet()){
            pageName = String.valueOf(o);
            visitorMapper.updateVisitorBypageName(pageName,String.valueOf(map.get(o)));
            if(!"totalVisitor".equals(pageName) && !"yesterdayVisitor".equals(pageName)){
                hashRedisServiceImpl.hashDelete(StringUtil.VISITOR,pageName);
            }
        }
    }


}
