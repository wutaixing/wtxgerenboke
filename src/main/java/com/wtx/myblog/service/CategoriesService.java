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
    DataMap updateCategory(String categoryName, int type);

    DataMap findCategoriesNames();

    DataMap getCategoryArticle(int rows, int pageNum, String category);

    DataMap findCategoriesNameAndArticleNum();

    DataMap getAllCategoryArticle(int rows, int pageNum);

}
