package com.wtx.myblog.service.impl;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.LeaveMessageMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.LeaveMessage;
import com.wtx.myblog.model.User;
import com.wtx.myblog.model.UserReadNews;
import com.wtx.myblog.service.LeaveMessageService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.StringUtil;
import com.wtx.myblog.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
@Service
public class LeaveMessageServiceImpl implements LeaveMessageService {
    @Autowired
    private LeaveMessageMapper leaveMessageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HashRedisServiceImpl hashRedisServiceImpl;

    @Override
    public DataMap publishLeaveMessage(String username, String leaveMessageContent, String pageName) {
        LeaveMessage leaveMessage = new LeaveMessage();
        //获取用户id
        int userId = userMapper.getUserIdByUsername(username);
        leaveMessage.setPId(0);//留言的父id 若是留言则为 0，则是留言中的回复 则为对应留言的id
        leaveMessage.setAnswererId(userId);// 留言者
        leaveMessage.setRespondentId(1);// 被回复者
        TimeUtil timeUtil = new TimeUtil();
        leaveMessage.setLeaveMessageDate(timeUtil.getFormatDateForFive());
        leaveMessage.setLikes(0);
        leaveMessage.setLeaveMessageContent(leaveMessageContent);
        leaveMessage.setIsRead(0);
        leaveMessage.setPageName(pageName);
        leaveMessageMapper.publishLeaveMessage(leaveMessage);

        //更新redis中的数据
        addRedisNews(leaveMessage);
        // 发表留言后，获取并返回更新后的留言列表
        DataMap pageLeaveMessage = getPageLeaveMessage(pageName, username);
        return DataMap.success(CodeType.SUCCESS_STATUS).message("留言发布成功").setData(pageLeaveMessage.getData());
    }

    // 在redis中添加一条数据
    private void addRedisNews(LeaveMessage leaveMessage) {
        if(leaveMessage.getRespondentId()!= leaveMessage.getAnswererId()){
            boolean isExistKey = hashRedisServiceImpl.hasKey(leaveMessage.getRespondentId() + StringUtil.BLANK);
            if(! isExistKey){
                UserReadNews userReadNews = new UserReadNews(1,0,1);
                hashRedisServiceImpl.put(String.valueOf(leaveMessage.getRespondentId()), userReadNews,UserReadNews.class);
            }else {
                hashRedisServiceImpl.hashIncrement(leaveMessage.getRespondentId() + StringUtil.BLANK, "allNewsNum", 1);
                hashRedisServiceImpl.hashIncrement(leaveMessage.getRespondentId() + StringUtil.BLANK, "leaveMessageNum", 1);
            }
        }
    }

    @Override
    public DataMap getPageLeaveMessage(String pageName,String username) {
        List<LeaveMessage> leaveMessageList = leaveMessageMapper.getAllLeaveMessage(pageName);
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        HashMap<String, Object> map;
        TimeUtil timeUtil = new TimeUtil();
        for (LeaveMessage leaveMessage : leaveMessageList) {
            map = new HashMap<>();;
            map.put("id", leaveMessage.getId());
            map.put("avatarImgUrl", userMapper.getAvatarImgUrlById(leaveMessage.getAnswererId()));
            map.put("answerer", userMapper.getUserNameById(leaveMessage.getAnswererId()));
            map.put("leaveMessageDate", timeUtil.stringToDateThree(leaveMessage.getLeaveMessageDate()));
            map.put("leaveMessageContent", leaveMessage.getLeaveMessageContent());
            //判断当前登录用户是否点赞评论
            int isRead = 0;
            if (username != null && !username.isEmpty()) {
                int userId = userMapper.getUserIdByUserName(username);
                isRead = leaveMessageMapper.getIsRead(pageName, leaveMessage.getId(), userId);
            }
            map.put("isLiked", isRead > 0 ? 1 : 0);
            map.put("likes", leaveMessage.getLikes());
            // 获取回复
            ArrayList<HashMap<String, Object>> replies = new ArrayList<>();
            HashMap<String, Object> repliesMap;
            if (leaveMessage.getPId() != 0){
                List<LeaveMessage> replyList = leaveMessageMapper.getAllLeaveMessageReply(pageName, leaveMessage.getPId());
                for (LeaveMessage replyMessage : replyList) {
                    repliesMap = new HashMap<>();
                    repliesMap.put("id",replyMessage.getPId());
                    repliesMap.put("answerer", userMapper.getUserNameById(replyMessage.getAnswererId()));
                    repliesMap.put("respondent", userMapper.getUserNameById(replyMessage.getRespondentId()));
                    repliesMap.put("leaveMessageContent", leaveMessageMapper.findLeaveMessageById(replyMessage.getPId()));
                    repliesMap.put("leaveMessageDate", timeUtil.stringToDateThree(leaveMessageMapper.findleaveMessageDateById(replyMessage.getPId())));
                    replies.add(repliesMap);
                }
            }
            map.put("replies",replies);
            resultList.add(map);
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("result", resultList);
        return DataMap.success().setData(dataMap);
    }
}
