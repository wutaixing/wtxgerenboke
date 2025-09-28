package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.service.IndexService;
import com.wtx.myblog.service.RedisService;
import com.wtx.myblog.service.impl.RedisServiceImpl;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

/**
 * @author 26989
 * @date 2025/9/23
 * @description
 */
@RestController
@Slf4j
public class IndexController {

    @Autowired
    private IndexService indexService;
    @Autowired
    private RedisServiceImpl redisService;

    /**
     * @description: // 首页获取网站信息
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/23 19:52
    */
    @GetMapping("/getSiteInfo")
    public String getSiteInfo(HttpServletRequest request) {
        try {
            DataMap data = indexService.getSiteInfo(request);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【IndexController】getSiteInfo Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: // 首页获取最新评论信息
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/23 19:53
    */
    @GetMapping("/newComment")
    public String newComment(HttpServletRequest request,@RequestParam(value = "rows") int rows, @RequestParam(value = "pageNum") int pageNum) {
        try {
            DataMap data = indexService.newComment(request,rows, pageNum);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【IndexController】newComment Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: // 获取最新留言信息
     * @param: request
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/24 10:39
    */
    @GetMapping("/newLeaveWord")
    public String newLeaveWord(HttpServletRequest request,@RequestParam(value = "rows") int rows, @RequestParam(value = "pageNum") int pageNum) {
        try {
            DataMap data = indexService.newLeaveWord(request,rows, pageNum);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【IndexController】newLeaveWord Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: // 获取标签云信息
     * @param: request
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/24 10:40
    */
    @GetMapping("/findTagsCloud")
    public String findTagsCloud(HttpServletRequest request) {
        try {
            DataMap data = indexService.findTagsCloud(request);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【IndexController】findTagsCloud Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 首页获取用户信息
     * @param: principal
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/24 10:58
    */
    @PostMapping("/getUserNews")
    public String getUserNews(@AuthenticationPrincipal Principal principal, HttpServletRequest request) {
        try {
            // 检查 @AuthenticationPrincipal 参数
            if (principal != null) {
                String username = principal.getName();
                DataMap data = redisService.getUserNews(username);
                return JsonResult.build(data).toJSON();
            } else {
                // 如果 @AuthenticationPrincipal 为 null，尝试从 request 中获取
                Principal userPrincipal = request.getUserPrincipal();
                if (userPrincipal != null) {
                    String username = userPrincipal.getName();
                    DataMap data = redisService.getUserNews(username);
                    return JsonResult.build(data).toJSON();
                } else {
                    // 用户未登录
                    return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
                }
            }
        } catch (Exception e) {
            log.error("【IndexController】getUserNews Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
