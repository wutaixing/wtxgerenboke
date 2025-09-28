package com.wtx.myblog.service.impl;

import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.mapper.VisitorMapper;
import com.wtx.myblog.model.Visitor;
import com.wtx.myblog.service.StatisticsService;
import com.wtx.myblog.service.UserService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


/**
 * @author 26989
 * @date 2025/9/21
 * @description 统计服务实现类 - Redis+MySQL双重存储
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    //总访问人数
    private static final String TOTAL_VISITOR = "totalVisitor";
    //当前页的访问人数
    private static final String PAGE_VISITOR = "pageVisitor";
    @Autowired
    private RedisServiceImpl redisServiceImpl;
    @Autowired
    private VisitorMapper visitorMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StringRedisServiceImpl stringRedisServiceImpl;

    @Override
    public DataMap getVisitorNumByPageName(String pageName, HttpServletRequest request) {
        //创建一个 Map
        HashMap<String, Object> dataMap = new HashMap<>();

        //获取当前访问页面的visitor
        String visitor = (String) request.getSession().getAttribute(pageName);
        //判断session生命周期中是否浏览过当前page,则增加访问量
        if (visitor == null) {
            request.getSession().setAttribute(pageName,"yes");
        }
        //增加当前页面的访问人数
        long pageVisitor = updatePageVisitor(visitor,pageName);
        //增加总访问人数
        long totalVisitor = redisServiceImpl.addVisitorNumOnRedis(StringUtil.VISITOR,TOTAL_VISITOR,1);
        if(totalVisitor == 0){
            totalVisitor = visitorMapper.getTotalVisitor();
            //redis 中要去put一下
            totalVisitor = redisServiceImpl.putVistorNumOnRedis(StringUtil.VISITOR,TOTAL_VISITOR,totalVisitor + 1);
        }
        dataMap.put(PAGE_VISITOR,pageVisitor);
        dataMap.put(TOTAL_VISITOR,totalVisitor);
        return DataMap.success().setData(dataMap);
    }

    @Override
    public DataMap getStatisticsInfo(HttpServletRequest request) {
        HashMap<String, Object> dataMap = new HashMap<>();
        // 获取总访量
        long allVisitor = redisServiceImpl.getVisitorOnRedis(StringUtil.VISITOR,"totalVisitor");
        // 昨日访问量
        long yesterdayVisitor = redisServiceImpl.getVisitorOnRedis(StringUtil.VISITOR,"yesterdayVisitor");

        dataMap.put("allVisitor",allVisitor);
        dataMap.put("yesterdayVisitor",yesterdayVisitor);
        dataMap.put("allUser",userMapper.countUserNum());
        dataMap.put("articleNum",articleMapper.countarticleNum());
        //点赞量查询 TODO
        if (stringRedisServiceImpl.hasKey(StringUtil.ARTICLE_THUMBS_UP)){
            int articleThumbsUpNum = (int)stringRedisServiceImpl.get(StringUtil.ARTICLE_THUMBS_UP);
            dataMap.put("articleThumbsUpNum",articleThumbsUpNum);
        }else {
            dataMap.put("articleThumbsUpNum",0);
        }
        return DataMap.success().setData(dataMap);
    }

    /**
     * @description: //TODO更新当前页面访问量
     * @param: vistor
     * @param: pageName
     * @return: long
     * @author 26989
     * @date: 2025/9/23 11:46
    */
    private long updatePageVisitor(String visitor, String pageName) {
        Visitor thisVisitor;
        Long pageVisitor;
        //session生命周期内没有浏览器修改page,则增加访问量
        if (visitor == null) {
            pageVisitor = redisServiceImpl.addVisitorNumOnRedis(StringUtil.VISITOR,pageName,1);
            //如果从redis中没有命中，则从数据库中取
            if(pageVisitor == 0){
                thisVisitor = visitorMapper.getVisitorNumByPageName(pageName);
                if(thisVisitor != null){
                    //thisVisitor
                    redisServiceImpl.putVistorNumOnRedis(StringUtil.VISITOR,pageName,pageVisitor + 1);
                }else {
                    return 0l;
                }
            }
        }
        return 0l;
    }
}
