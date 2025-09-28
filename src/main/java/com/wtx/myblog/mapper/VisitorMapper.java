package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 26989
 * @date 2025/9/23
 * @description ]
 */
@Mapper
@Repository
public interface VisitorMapper {
    long getTotalVisitor();

    Visitor getVisitorNumByPageName(@Param("pageName") String pageName);

    void insertVisitorArticlePage(@Param("pageName") String pageName);

    void updateVisitorBypageName(@Param("pageName") String pageName, @Param("visitorNum") String visitorNum);
}
