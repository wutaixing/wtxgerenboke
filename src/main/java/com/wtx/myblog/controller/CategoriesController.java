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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * @description: //TODO 查询分类列表接口
     * @param: 
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/11 16:23
    */
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
    
    /**
     * @description: //TODO 根据分类名称修改分类接口
     * @param: categoryName
     * @param: type
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/11 16:49
    */
    @PostMapping("/updateCategory")
    public String updateCategory(@RequestParam(value = "categoryName") String categoryName,@RequestParam (value = "type") int type){
        //type 1:增加 2:删除
        try {
            DataMap data = categoriesService.updateCategory(categoryName, type);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("CategoriesController updateCategory", e);
        }
        // 失败
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 查询所有分类名称接口(发表文章页面 下拉列表)
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/11 17:13
    */
    @GetMapping("/findCategoriesName")
    public String findCategoriesName(){
        try {
            DataMap data = categoriesService.findCategoriesNames();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("CategoriesController findCategoriesName", e);
        }
        // 失败
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //获得分类文章
     * @param: request
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/25 13:31
    */
    @GetMapping("/getCategoryArticle")
    public String getCategoryArticle(HttpServletRequest request, @RequestParam(value = "rows") int rows, @RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "category") String category) {
        try {
            if (category == null || category.trim().isEmpty()) {
                DataMap data = categoriesService.getAllCategoryArticle(rows, pageNum);
                return JsonResult.build(data).toJSON();
            }else {
                DataMap data = categoriesService.getCategoryArticle(rows, pageNum, category);
                return JsonResult.build(data).toJSON();
            }
        } catch (Exception e) {
            log.error("【CategoriesController】getCategoryArticle Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }


    @GetMapping("/findCategoriesNameAndArticleNum")
    public String findCategoriesNameAndArticleNum(HttpServletRequest request) {
        try {
            DataMap data = categoriesService.findCategoriesNameAndArticleNum();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【CategoriesController】findCategoriesNameAndArticleNum Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }


}
