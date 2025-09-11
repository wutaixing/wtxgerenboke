package com.wtx.myblog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.CategoriesMapper;
import com.wtx.myblog.model.Categories;
import com.wtx.myblog.service.CategoriesService;
import com.wtx.myblog.utils.DataMap;
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
    @Transactional
    public DataMap insert(String categoryName) {
        try {
            // 查询分类是否已存在
            Categories category = categoriesMapper.findByCategoryName(categoryName);
            if (category != null){
                return DataMap.fail(CodeType.CATEGORY_EXIST);
            }
            int result = categoriesMapper.insert(categoryName);
            if (result > 0){
                return DataMap.success(CodeType.ADD_CATEGORY_SUCCESS);
            }else {
                return DataMap.fail(CodeType.SERVER_EXCEPTION);
            }
        } catch (Exception e) {
            log.error("【添加分类】发生异常！", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public DataMap delete(String categoryName) {
        try {
            Categories category = categoriesMapper.findByCategoryName(categoryName);
            if (category == null){
                return DataMap.fail(CodeType.CATEGORY_NOT_EXIST);
            }
            int result = categoriesMapper.delete(categoryName);
            if (result > 0){
                return DataMap.success(CodeType.DELETE_CATEGORY_SUCCESS);
            }else{
                return DataMap.fail(CodeType.CATEGORY_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error("【删除分类】发生异常！", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION);
        }
    }
}
