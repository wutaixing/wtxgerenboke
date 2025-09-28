package com.wtx.myblog.service.impl;

import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.service.RedisService;
import com.wtx.myblog.utils.DataMap;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author 26989
 * @date 2025/9/23
 * @description
 */
@Service
public class RedisServiceImpl{
    @Autowired
    private HashRedisServiceImpl hashRedisService;
    @Autowired
    private StringRedisServiceImpl stringRedisService;
    @Autowired
    private UserMapper userMapper;

    /**
     * @description: //TODO 增加redis中访问量
     * @param: visitor
     * @param: totalVisitor
     * @param: i
     * @return: long
     * @author 26989
     * @date: 2025/9/23 12:12
    */
    public long addVisitorNumOnRedis(String visitor, String field, long i) {
        boolean fieIdIsExist = hashRedisService.hasHashKey(visitor, field);
        if (fieIdIsExist) {
            return hashRedisService.hashIncrement(visitor, field, i);
        }
        return 0l;
    }

    /**
     * @description: //TODO在redis中保存访问量
     * @param: visitor
     * @param: field
     * @param: i
     * @return: long
     * @author 26989
     * @date: 2025/9/23 12:25
    */
    public long putVistorNumOnRedis(String visitor, String field, Object value) {
        hashRedisService.put(visitor, field, value);
        return Long.valueOf(hashRedisService.get(visitor, field).toString());
    }

    /**
     * @description: 获取redis中访问量
     * @param: visitor
     * @param: totalVisitor
     * @return: long
     * @author 26989
     * @date: 2025/9/23 16:42
    */
    public long getVisitorOnRedis(String visitor, String totalVisitor) {
        boolean fieldIsExist = hashRedisService.hasHashKey(visitor, totalVisitor);
        if (fieldIsExist) {
            return Long.valueOf(hashRedisService.get(visitor, totalVisitor).toString());
        }
        return 0l;
    }

    /**
     * @description: 跟新redis中未读量
     * @param: articleThumbsUp
     * @param: i
     * @return: void
     * @author 26989
     * @date: 2025/9/23 16:42
    */
    public void readThumbsUpOnRedis(String articleThumbsUp, int i) {
        Boolean readThumbsUpRedisIsExist = stringRedisService.hasKey(articleThumbsUp);
        if (readThumbsUpRedisIsExist) {
            stringRedisService.set(articleThumbsUp, 1);
        } else {
            stringRedisService.stringIncrement(articleThumbsUp, i);
        }

    }

    /**
     * @description: // 获取用户未读消息
     * @param: username
     * @return: com.wtx.myblog.utils.DataMap
     * @author 26989
     * @date: 2025/9/24 15:01
    */
    public DataMap getUserNews(String username) {
        //封装数据
        HashMap<String, Object> map = new HashMap<>();
        //根据当前登录用户查询用户id
        int userId = userMapper.getUserIdByUserName(username);
        //根据用户id查询redis中数据
        LinkedHashMap allFieldAndValue = (LinkedHashMap) hashRedisService.getAllFieldAndValue(String.valueOf(userId));
        JSONObject jsonObject = new JSONObject();
        if(allFieldAndValue.size() == 0){
            map.put("result",0);
        }else {
            int allNewsNum = (int) allFieldAndValue.get("allNewsNum");
            int commentNum = (int) allFieldAndValue.get("commentNum");
            int leaveMessageNum = (int) allFieldAndValue.get("leaveMessageNum");
            jsonObject.put("allNewsNum",allNewsNum);
            jsonObject.put("commentNum",commentNum);
            jsonObject.put("leaveMessageNum",leaveMessageNum);
            map.put("result",jsonObject);
        }
        return DataMap.success().setData(map);
    }
}
