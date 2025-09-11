package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Categories;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Mapper
@Repository
public interface CategoriesMapper {

    List<Categories> getArticleCategories();

    List<String> findCategoriesNames();

    int findIsExistByCategoryName(String categoryName);

    void saveCategories(Categories categories);

    void deleteCategory(String categoryName);

}
