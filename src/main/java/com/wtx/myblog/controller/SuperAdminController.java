package com.wtx.myblog.controller;

import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 26989
 * @date 2025/9/8
 * @description
 */
@RestController
public class SuperAdminController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/getArticleManagement")
    public String getArticleManagement(Integer rows,Integer pageNum) {
        DataMap dataMap = articleService.getAllArticle(rows, pageNum);
        return JsonResult.build(dataMap).toJSON();
    }


    @GetMapping("/deleteArticle")
    public String deleteArticle(Integer id) {
        DataMap dataMap = articleService.deleteArticleByid(id);
        return JsonResult.build(dataMap).toJSON();
    }
}
