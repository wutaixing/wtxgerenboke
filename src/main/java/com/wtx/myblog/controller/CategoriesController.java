package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.model.Categories;
import com.wtx.myblog.service.CategoriesService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description  分类管理控制器
 */
@RestController
@Slf4j
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/getArticleCategories")
    public String getArticleCategories() {
        try {
            DataMap data = categoriesService.getArticleCategories();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("CategoriesController getArticleCategories", e);
        }
        // 失败
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    @PostMapping("/updateCategory")
    public String updateCategory(String categoryName,String type){
        //type 1:增加 2:删除
        DataMap result = null;
        if("1".equals(type)){
            result = categoriesService.insert(categoryName);
        }else if("2".equals(type)){
            result = categoriesService.delete(categoryName);
        }
        return JsonResult.build(result).toJSON();
    }
}
