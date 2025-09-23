package com.wtx.myblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.mapper.FeedbackMappwer;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.FeedBack;
import com.wtx.myblog.model.PrivateWord;
import com.wtx.myblog.service.FeedbackService;
import com.wtx.myblog.service.UserService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.StringUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/21
 * @description
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMappwer feedbackMappwer;
    @Autowired
    private UserMapper userMapper;

    @Override
    public DataMap getAllFeedback(int rows, int pageNum) {
        PageHelper.startPage(pageNum, rows);
        List<FeedBack> feedBackList = feedbackMappwer.getAllFeedback();
        PageInfo<FeedBack> pageInfo = new PageInfo<>(feedBackList);
        //返回数据处理
        JSONArray returnJsonArray = new JSONArray();
        JSONObject returnJsonObject = new JSONObject();
        //创建一个articleJson
        JSONObject articleJson;
        for (FeedBack feedBack : feedBackList) {
            articleJson = new JSONObject();
            articleJson.put("person", userMapper.findUsernameById(feedBack.getPersonId()));
            articleJson.put("feedbackDate", feedBack.getFeedbackDate());
            articleJson.put("feedbackContent", feedBack.getFeedbackContent());
            if (feedBack.getContactInfo() == null) {
                articleJson.put("contactInfo", StringUtil.BLANK);
            } else {
                articleJson.put("contactInfo", feedBack.getContactInfo());
            }
            returnJsonArray.add(articleJson);
        }
        returnJsonObject.put("result", returnJsonArray);
        JSONObject pageJson = new JSONObject();
        pageJson.put("pageNum", pageInfo.getPageNum());
        pageJson.put("pageSize", pageInfo.getPageSize());
        pageJson.put("pages", pageInfo.getPages());
        pageJson.put("total", pageInfo.getTotal());
        pageJson.put("isFirstPage", pageInfo.isIsFirstPage());
        pageJson.put("isLastPage", pageInfo.isIsLastPage());
        returnJsonObject.put("pageInfo", pageJson);
        return DataMap.success().setData(returnJsonObject);
    }

    @Override
    public DataMap getAllPrivateWord() {
        List<PrivateWord> privateWordList = feedbackMappwer.getAllPrivateWord();
        //封装数据
        JSONObject returnJson = new JSONObject();
        JSONArray returnJsonArray = new JSONArray();
        JSONObject userJson;
        for (PrivateWord privateWord : privateWordList) {
            userJson = new JSONObject();
            userJson.put("privateWord", privateWord.getPrivateWord());
            userJson.put("publisher", userMapper.findUsernameById(privateWord.getPublisherId()));
            userJson.put("publisherDate", privateWord.getPublisherDate());
            if (privateWord.getReplyContent() == null){
                userJson.put("replyContent", StringUtil.BLANK);
            }else {
                userJson.put("replyContent", privateWord.getReplyContent());
            }
            returnJsonArray.add(userJson);
        }
        //外层封装
        returnJson.put("result", returnJsonArray);
        return DataMap.success().setData(returnJson);
    }

}
