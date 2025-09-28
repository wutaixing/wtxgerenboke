package com.wtx.myblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.IndexMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.model.Comment;
import com.wtx.myblog.model.LeaveMessage;
import com.wtx.myblog.model.Tags;
import com.wtx.myblog.service.IndexService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/23
 * @description
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public DataMap getSiteInfo(HttpServletRequest request) {
        //获取文章总数
        long articleNum = indexMapper.getarticleNum();
        //获取标签总数
        long tagsNum = indexMapper.gettagsNum();
        //留言总数
        long messageNum = indexMapper.getmessageNum();
        //评论总数
        long commentNum = indexMapper.getcommentNum();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("articleNum",articleNum);
        dataMap.put("tagsNum",tagsNum);
        dataMap.put("leaveWordNum",messageNum);
        dataMap.put("commentNum",commentNum);
        return DataMap.success().setData(dataMap);
    }

    @Override
    public DataMap newComment(HttpServletRequest request, int rows, int pageNum) {
        //开启分页插件
        PageHelper.startPage(pageNum, rows);
        //查询评论并且存入集合中
        List<Comment> commentList = indexMapper.getAllCommentOrderByDate();
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        //返回数据处理
        JSONArray returnJsonArray = new JSONArray();
        JSONObject returnJsonObject = new JSONObject();
        //创建一个articleJson
        JSONObject articleJson;
        //TimeUtil timeUtil = new TimeUtil();
        for (Comment comment : commentList) {
            articleJson = new JSONObject();
            articleJson.put("id", comment.getId());
            articleJson.put("articleId", comment.getArticleId());
            articleJson.put("articleTitle", articleMapper.getArticleByTitleArticleId(comment.getArticleId()));
            articleJson.put("commentContent", comment.getCommentContent());
            articleJson.put("answerer", userMapper.getUserNameById(comment.getAnswererId()));//评论者名称
            //articleJson.put("commentDate", timeUtil.stringToDateThree(comment.getCommentDate()));
            articleJson.put("commentDate", comment.getCommentDate().substring(0,10));
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
    public DataMap newLeaveWord(HttpServletRequest request, int rows, int pageNum) {
        //开启分页插件
        PageHelper.startPage(pageNum, rows);
        //查询留言并且存入集合中
        List<LeaveMessage> leaveMessageList = indexMapper.getAllLeaveMessageOrderByDate();
        PageInfo<LeaveMessage> pageInfo = new PageInfo<>(leaveMessageList);
        //返回数据处理
        JSONArray returnJsonArray = new JSONArray();
        JSONObject returnJsonObject = new JSONObject();
        //创建一个articleJson
        JSONObject articleJson;
        //TimeUtil timeUtil = new TimeUtil();
        for (LeaveMessage leaveMessage : leaveMessageList) {
            articleJson = new JSONObject();
            articleJson.put("id", leaveMessage.getId());
            articleJson.put("pagePath", leaveMessage.getPageName());
            articleJson.put("answerer",userMapper.getUserNameById(leaveMessage.getAnswererId()));
            articleJson.put("leaveWordContent", leaveMessage.getLeaveMessageContent());
            articleJson.put("leaveWordDate", leaveMessage.getLeaveMessageDate().substring(0,10));
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
    public DataMap findTagsCloud(HttpServletRequest request) {
        //查询标签并且存入集合中
        List<Tags> leaveTagsList = indexMapper.getAllTagsCloud();
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", JSONArray.fromObject(leaveTagsList));
        return DataMap.success(CodeType.FIND_TAGS_CLOUD).setData(map);
    }

    @Override
    public DataMap getUserNews(HttpServletRequest request) {
        // 未读评论数量（用于用户页面评论标签）
        long commentNum = indexMapper.getCommentNumNotReadNum();
        // 未读留言数量（用于用户页面留言标签）
        long leaveMessageNum = indexMapper.getMessageNotReadNum();
        // 所有未读消息总数（用于顶部徽章显示）
        long allNewsNum = commentNum + leaveMessageNum;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("allNewsNum", allNewsNum);
        dataMap.put("commentNum", commentNum);
        dataMap.put("leaveMessageNum", leaveMessageNum);
        HashMap<String, Object> data = new HashMap<>();
        data.put("result", dataMap);
        return DataMap.success().setData(data);
    }


}
