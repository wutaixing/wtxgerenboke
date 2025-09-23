package com.wtx.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.LikeMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.ArticleLikesRecord;
import com.wtx.myblog.model.FriendLink;
import com.wtx.myblog.service.LikeService;
import com.wtx.myblog.utils.DataMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/17
 * @description
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public DataMap getArticleThumbsUp(int rows, int pageNum) {
        PageHelper.startPage(pageNum, rows);
        List<ArticleLikesRecord>  likesRecord = likeMapper.getArticleThumbsUp();
        PageInfo<ArticleLikesRecord> pageInfo = new PageInfo<>(likesRecord);
        JSONObject returnJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject articleLikesJson;
        for (ArticleLikesRecord articleLikesRecord : likesRecord) {
            articleLikesJson = new JSONObject();
            articleLikesJson.put("id", articleLikesRecord.getId());
            articleLikesJson.put("articleId", articleLikesRecord.getArticleId());
            articleLikesJson.put("articleTitle", articleMapper.getArticleByTitleArticleId(articleLikesRecord.getArticleId()));
            articleLikesJson.put("likeDate", articleLikesRecord.getLikeDate());
            //articleLikesJson.put("praisePeople", articleMapper.getArticleAuthorByArticleId(articleLikesRecord.getArticleId()));
            articleLikesJson.put("praisePeople", userMapper.getUserNameById(articleLikesRecord.getLikerId()));
            articleLikesJson.put("isRead", articleLikesRecord.getIsRead());
            jsonArray.add(articleLikesJson);
        }
        returnJson.put("result", jsonArray);
        returnJson.put("msgIsNotReadNum",likeMapper.getMsgIsNotReadNum());
        //封装最外层数据
        JSONObject pageJson = new JSONObject();
        pageJson.put("pageNum", pageInfo.getPageNum());
        pageJson.put("pageSize", pageInfo.getPageSize());
        pageJson.put("pages", pageInfo.getPages());
        pageJson.put("total", pageInfo.getTotal());
        pageJson.put("isFirstPage", pageInfo.isIsFirstPage());
        pageJson.put("isLastPage", pageInfo.isIsLastPage());
        returnJson.put("pageInfo", pageJson);
        return DataMap.success().setData(returnJson);
    }

    @Override
    public DataMap readAllThumbsUp() {
        likeMapper.readAllThumbsUp();
        //TODO 删除redis中的点赞信息
        return DataMap.success();
    }

    @Override
    public DataMap getFriendLink() {
        List<FriendLink> friendLinks = likeMapper.getFriendLink();
        return DataMap.success().setData(friendLinks);
    }

    @Transactional
    @Override
    public DataMap addFriendLink(FriendLink friendLink) {
        //int id = likeMapper.findIsExistBylogger(friendLink.getBlogger());
        likeMapper.addFriendLink(friendLink);
        return DataMap.success(CodeType.ADD_FRIEND_LINK_SUCCESS).setData(friendLink.getId());
    }

    @Override
    public DataMap updateFriendLink(FriendLink friendLink, String id) {
        likeMapper.updateFriendLink(friendLink, id);
        return DataMap.success(CodeType.UPDATE_FRIEND_LINK_SUCCESS).setData(friendLink.getId());
    }

    @Override
    public DataMap deleteFriendLink(String id) {
        likeMapper.deleteFriendLink(id);
        return DataMap.success(CodeType.DELETE_FRIEND_LINK_SUCCESS);
    }
}
