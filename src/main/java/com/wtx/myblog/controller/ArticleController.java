package com.wtx.myblog.controller;

import com.wtx.myblog.model.vo.ArticleRequestVO;
import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 26989
 * @date 2025/9/11
 * @description 博客文章控制类
 */
@RestController
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * @description: //TODO 发博客功能接口
     * @param: articleRequestVO
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/11 18:09
    */
    @PostMapping("/publishArticle")
    //如果前端发送的是 JSON 数据，需要添加 @RequestBody 注解来接收。如果是表单数据（application/x-www-form-urlencoded），则不需要注解，Spring 会自动绑定参数。
    public String publishArticle(ArticleRequestVO articleRequestVO) { 
        DataMap dataMap = articleService.insertArticle(articleRequestVO);
        return JsonResult.build(dataMap).toJSON();
    }
}
