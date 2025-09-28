package com.wtx.myblog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.CategoriesMapper;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.model.Categories;
import com.wtx.myblog.model.Comment;
import com.wtx.myblog.service.CategoriesService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.StringAndArray;
import com.wtx.myblog.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Service
public class CategoriesServiceImpl implements CategoriesService {

    private static final Logger log = LoggerFactory.getLogger(CategoriesServiceImpl.class);
    @Autowired
    private CategoriesMapper categoriesMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public DataMap getArticleCategories() {
        //查询分类数据
        List<Categories> categories = categoriesMapper.getArticleCategories();
        //处理查询出来的数据
        JSONObject returnJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Categories category : categories) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", category.getId());
            jsonObject.put("categoryName", category.getCategoryName());
            //将封装好的json对象添加到jsonArray中
            jsonArray.add(jsonObject);
        }
        returnJson.put("result", jsonArray);
        return DataMap.success().setData(returnJson);
    }

    @Override
    public DataMap updateCategory(String categoryName, int type) {

        int isExistCategoryName = categoriesMapper.findIsExistByCategoryName(categoryName);
        // 判断是新增还是删除
        if(type == 1){
            // 如果是1则为新增
            if(isExistCategoryName == 0){
                // 没有查询到分类
                Categories categories = new Categories();
                categories.setCategoryName(categoryName);
                categoriesMapper.saveCategories(categories);
                int newCategoriesId = categoriesMapper.findIsExistByCategoryName(categoryName);
                return DataMap.success(CodeType.ADD_CATEGORY_SUCCESS).setData(newCategoriesId);
            }else{
                return DataMap.fail(CodeType.CATEGORY_EXIST);
            }
        }else{
            // 删除
            if(isExistCategoryName != 0){
                //TODO 查询到分类下面对应有多少文章
                // 如果查询出来的文章数量不为空，则返回提示”分类下存在文章，删除失败“
                categoriesMapper.deleteCategory(categoryName);
                return DataMap.success(CodeType.DELETE_CATEGORY_SUCCESS);
            }else{
                return DataMap.fail(CodeType.CATEGORY_NOT_EXIST);
            }
        }
    }

    @Override
    public DataMap findCategoriesNames() {
        List<String> categoriesNames = categoriesMapper.findCategoriesNames();
        return DataMap.success().setData(categoriesNames);
    }

    @Override
    public DataMap getCategoryArticle(int rows, int pageNum, String category) {
        //开启分页插件
        PageHelper.startPage(pageNum, rows);
        //查询评论并且存入集合中
        List<Article> articleList = articleMapper.getArticleByArticleCategories(category);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        //返回数据处理
        net.sf.json.JSONArray returnJsonArray = new net.sf.json.JSONArray();
        JSONObject returnJsonObject = new JSONObject();
        //创建一个articleJson
        JSONObject articleJson;
        //TimeUtil timeUtil = new TimeUtil();
        for (Article article : articleList) {
            articleJson = new JSONObject();
            articleJson.put("id", article.getId());
            articleJson.put("articleId", article.getArticleId());
            articleJson.put("articleTitle", article.getArticleTitle());
            articleJson.put("publishDate", article.getPublishDate());
            articleJson.put("articleCategories", article.getArticleCategories());
            articleJson.put("articleTags", StringAndArray.stringToArray(article.getArticleTags()));
            returnJsonArray.add(articleJson);
        }
        returnJsonObject.put("category", category);
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
    public DataMap getAllCategoryArticle(int rows, int pageNum) {
        //开启分页插件
        PageHelper.startPage(pageNum, rows);
        //查询评论并且存入集合中
        List<Article> articleList = articleMapper.getAllArticleByArticleCategories();
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        //返回数据处理
        net.sf.json.JSONArray returnJsonArray = new net.sf.json.JSONArray();
        JSONObject returnJsonObject = new JSONObject();
        //创建一个articleJson
        JSONObject articleJson;
        //TimeUtil timeUtil = new TimeUtil();
        for (Article article : articleList) {
            articleJson = new JSONObject();
            articleJson.put("id", article.getId());
            articleJson.put("articleId", article.getArticleId());
            articleJson.put("articleTitle", article.getArticleTitle());
            articleJson.put("publishDate", article.getPublishDate());
            articleJson.put("articleCategories", article.getArticleCategories());
            articleJson.put("articleTags", StringAndArray.stringToArray(article.getArticleTags()));
            returnJsonArray.add(articleJson);
        }
        returnJsonObject.put("category", "");
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
    public DataMap findCategoriesNameAndArticleNum() {
        //响应数据
        ArrayList<Object> list = new ArrayList<>();
        HashMap<String, Object> map;
        List<String> categoriesNames = categoriesMapper.findCategoriesNames();
        for (String categoriesName : categoriesNames) {
            map= new HashMap<>();
            map.put("categoryName", categoriesName);
            int articleNum = articleMapper.findArticleNumByCategoryName(categoriesName);
            map.put("categoryArticleNum", articleNum);
            list.add(map);
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("result",list);
        return DataMap.success().setData(dataMap);
    }




}
