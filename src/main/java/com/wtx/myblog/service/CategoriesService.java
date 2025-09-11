package com.wtx.myblog.service;

import com.wtx.myblog.model.Categories;
import com.wtx.myblog.utils.DataMap;

import java.util.HashMap;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
public interface CategoriesService {
    DataMap getArticleCategories();

    DataMap insert(String categoryName);

    DataMap delete(String categoryName);
}
