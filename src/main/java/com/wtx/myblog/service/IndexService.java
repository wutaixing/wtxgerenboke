package com.wtx.myblog.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.model.Comment;
import com.wtx.myblog.utils.DataMap;
import net.sf.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/23
 * @description
 */
public interface IndexService {
    DataMap getSiteInfo(HttpServletRequest request);


    DataMap newComment(HttpServletRequest request, int rows, int pageNum);

    DataMap newLeaveWord(HttpServletRequest request, int rows, int pageNum);

    DataMap findTagsCloud(HttpServletRequest request);

    DataMap getUserNews(HttpServletRequest request);
}
