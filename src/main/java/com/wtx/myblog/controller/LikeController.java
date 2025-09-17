package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.service.LikeService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            log.error("【ArticleController】getArticleThumbsUp Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 跟新全部已读信息
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 23:48
     * */
    @GetMapping("/readAllThumbsUp")
    public String readAllThumbsUp() {
        try {
            DataMap data = likeService.readAllThumbsUp();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】readAllThumbsUp Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
