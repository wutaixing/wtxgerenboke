package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.model.ArticleLikesRecord;
import com.wtx.myblog.model.FriendLink;
import com.wtx.myblog.service.LikeService;
import com.wtx.myblog.service.RedisService;
import com.wtx.myblog.service.UserService;
import com.wtx.myblog.service.impl.RedisServiceImpl;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import com.wtx.myblog.utils.StringUtil;
import com.wtx.myblog.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author 26989
 * @date 2025/9/17
 * @description
 */
@RestController
@Slf4j
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisServiceImpl redisServiceImpl;

    /**
     * @description: //新增点赞
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/23 17:20
    */
    @GetMapping("/addArticleLike")
    public String addArticleLike(@RequestParam("articleId") String articleId, HttpServletRequest  request,@AuthenticationPrincipal Principal principal) {
        try {
            //获取UserName
            Principal userPrincipal = request.getUserPrincipal();
            String username = userPrincipal.getName();
            // 判断当前用户是否已点赞
            if(likeService.isLiked(Long.valueOf(articleId),username)){
                return JsonResult.fail(CodeType.ARTICLE_HAS_THUMBS_UP).toJSON();
            }
            DataMap data = likeService.updateLikedByArticleId(articleId);
            ArticleLikesRecord articleLikesRecord = new ArticleLikesRecord(Long.valueOf(articleId), userService.getUserIdByUserName(username),new TimeUtil().getFormatDateForFive(),1);
            likeService.insertArticleLikesRecord(articleLikesRecord);
            // 更新redis中的数据
            redisServiceImpl.readThumbsUpOnRedis(StringUtil.ARTICLE_THUMBS_UP, 1);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LikeController】getArticleThumbsUp Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 文章点赞管理接口
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 22:32
     */
    @PostMapping("/getArticleThumbsUp")
    public String getArticleThumbsUp(@RequestParam("rows") int rows, @RequestParam("pageNum") int pageNum) {
        try {
            DataMap data = likeService.getArticleThumbsUp(rows, pageNum);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LikeController】getArticleThumbsUp Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 跟新全部已读信息
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 23:48
     */
    @GetMapping("/readAllThumbsUp")
    public String readAllThumbsUp() {
        try {
            DataMap data = likeService.readAllThumbsUp();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LikeController】readAllThumbsUp Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 查询友链列表接口
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/20 21:50
     */
    @PostMapping("/getFriendLink")
    public String getFriendLink() {
        try {
            DataMap data = likeService.getFriendLink();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LikeController】getFriendLink Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 添加和编辑友链接口
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/20 22:06
     */
    @PostMapping("/updateFriendLink")
    public String updateFriendLink(@RequestParam("id") String id, @RequestParam("blogger") String blogger, @RequestParam("url") String url) {
        try {
            DataMap data;
            FriendLink friendLink = new FriendLink(blogger, url);
            if (StringUtil.BLANK.equals(id)){
                //新增
                data = likeService.addFriendLink(friendLink);
            }else {
                //编辑
                data = likeService.updateFriendLink(friendLink,id);
            }
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LikeController】updateFriendLink Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    @PostMapping("/deleteFriendLink")
    public String deleteFriendLink(@RequestParam("id") String id) {
        try {
            DataMap data = likeService.deleteFriendLink(id);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LikeController】updateFriendLink Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
