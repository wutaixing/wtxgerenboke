package com.wtx.myblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.StatisticsMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.utils.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    ArticleLikesService articleLikesService;
    @Qualifier("userMapper")
    @Autowired
    private UserMapper userMapper;

    @Override
    public DataMap insertArticle(Article article) {
        //还要处理剩余非空字段
        if (StringUtil.BLANK.equals(article.getArticleTitle())) {
            article.setOriginalAuthor(article.getAuthor());
        }
        if (StringUtil.BLANK.equals(article.getArticleUrl())) {
            //拼接一个url，如果是生产环境：www.javatiaozao.com
            article.setArticleUrl("www.javatiaozao.com" + "/article/" + article.getArticleId());
        }
        //TODO 设置上一篇文章id
        articleMapper.saveArticle(article);
        //给前端响应，封装数据
        //articleTitle updateDate articleUrl author
        HashMap<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("articleTitle", article.getArticleTitle());
        dataMap.put("updateDate", article.getUpdateDate());
        dataMap.put("articleUrl", "/article/" + article.getArticleId());
        dataMap.put("author", article.getAuthor());
        return DataMap.success().setData(dataMap);
    }

    @Override
    @Transactional
    public DataMap deleteArticle(String id) {
        //我们需要处理文章相关的所有东西
        //根据id查询文章信息
        Article article = articleMapper.getArticleById(id);
        //逻辑删除，实际数据并没有被删除，破坏查询条件，isDelete:0,1
        //物理删除：直接将数据库中的数据删除
        articleMapper.updateLastNextId("lastArticleId", article.getLastArticleId(), article.getNextArticleId());
        articleMapper.updateLastNextId("nextArticleId", article.getLastArticleId(), article.getNextArticleId());
        //删除本篇文章
        articleMapper.deleteArticle(article.getArticleId());
        //文章对应的点赞记录，评论，喜欢的记录删除
        //TODO
        return DataMap.success();
    }

    @Override
    public Article getArticleById(String id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public DataMap getDraftArticle(Article article, String[] tagStr, Integer tagsSizeByName) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", article.getId());
        dataMap.put("articleTitle", article.getArticleTitle());
        dataMap.put("articleContent", article.getArticleContent());
        dataMap.put("articleType", article.getArticleType());
        dataMap.put("articleGrade", tagsSizeByName);
        dataMap.put("articleUrl", article.getArticleUrl());
        dataMap.put("originalAuthor", article.getOriginalAuthor());
        dataMap.put("articleTags", tagStr);
        dataMap.put("articleCategories", article.getArticleCategories());
        return DataMap.success().setData(dataMap);
    }

    @Override
    public DataMap updateArticle(Article article) {
        Article newArticle = articleMapper.getArticleByIntId(article.getId());
        if ("原创".equals(newArticle.getArticleType())) {
            article.setOriginalAuthor(article.getAuthor());
            String url = "www.javatiaozao.com" + "/article/" + article.getArticleId();
            article.setArticleUrl(url);
        }
        articleMapper.updateArticleById(article);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("articleTitle", article.getArticleTitle());
        dataMap.put("updateDate", article.getUpdateDate());
        dataMap.put("articleUrl", article.getArticleId());
        dataMap.put("author", article.getAuthor());
        return DataMap.success().setData(dataMap);
    }

    @Override
    public DataMap getMyArticles(int rows, int pageNum) {
        PageHelper.startPage(pageNum, rows);
        List<Article> articleList = articleMapper.getArticleManagement();
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        ArrayList<Map<String, Object>> newArticles = new ArrayList<>();
        HashMap<String, Object> map;
        for (Article article : articleList) {
            map = new HashMap<>();
            map.put("thisArticleUrl", "/article/" + article.getArticleId());
            map.put("articleTitle", article.getArticleTitle());
            map.put("articleType", article.getArticleType());
            map.put("publishDate", article.getPublishDate());
            map.put("originalAuthor", article.getOriginalAuthor());
            map.put("articleCategories", article.getArticleCategories());
            map.put("articleTabloid", article.getArticleTabloid());//文章摘要
            map.put("articleTags", StringAndArray.stringToArray(article.getArticleTags()));
            map.put("likes", article.getLikes());
            newArticles.add(map);
        }
        JSONArray returnJsonArray = JSONArray.fromObject(newArticles);
        HashMap<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("pageNum", pageInfo.getPageNum());
        pageInfoMap.put("pageSize", pageInfo.getSize());
        pageInfoMap.put("pages", pageInfo.getPages());
        pageInfoMap.put("total", pageInfo.getTotal());
        pageInfoMap.put("isIsFirstPage", pageInfo.isIsFirstPage());
        pageInfoMap.put("isIsLastPage", pageInfo.isIsLastPage());
        returnJsonArray.add(pageInfoMap);
        return DataMap.success().setData(returnJsonArray);
    }

    @Override
    public Map<String, String> findArticleTitleByArticleId(long articleId) {
        Article articleInfo = articleMapper.getArticleByArticleId(articleId);
        HashMap<String, String> articleMap = new HashMap<>();
        if (articleInfo != null){
            articleMap.put("articleTitle", articleInfo.getArticleTitle());
            articleMap.put("articleTabloid", articleInfo.getArticleTabloid());
        }
        return articleMap;
    }

    @Override
    public DataMap getArticleByArticleId(long articleId, Integer userId) {
        Article article = articleMapper.getArticleByArticleId(articleId);
        if (article == null) {
            return DataMap.fail(CodeType.ARTICLE_NOT_EXIST);
        }else {
            // 检查用户是否已点赞
            boolean isLiked = false;
            if (userId != null) {
                try {
                    //检查点赞状态
                    isLiked = articleLikesService.hasUserLiked(articleId, userId);
                } catch (Exception e) {
                    System.err.println("检查用户点赞状态失败: " + e.getMessage());
                    // 如果检查失败，默认为未点赞
                    isLiked = false;
                }
            }
            article.setIsLiked(isLiked ? 1 : 0);
        }

        return DataMap.success().setData(article);
    }

    @Override
    public DataMap getArticleDetailByArticleId(long articleId, Integer userId) {
        try {
            Article article = articleMapper.getArticleByArticleId(articleId);
            if (article == null) {
                return DataMap.fail(CodeType.ARTICLE_NOT_EXIST);
            }

            // 构建文章详情数据
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("articleId", article.getArticleId());
            dataMap.put("articleTitle", article.getArticleTitle());
            dataMap.put("articleContent", article.getArticleContent());
            dataMap.put("articleType", article.getArticleType());
            dataMap.put("publishDate", article.getPublishDate());
            dataMap.put("originalAuthor", article.getOriginalAuthor());
            dataMap.put("articleCategories", article.getArticleCategories());
            dataMap.put("articleTags", StringAndArray.stringToArray(article.getArticleTags()));
            dataMap.put("articleUrl", "/article/" + article.getArticleId());
            dataMap.put("likes", article.getLikes());
            // 检查用户是否已点赞
            boolean isLiked = false;
            if (userId != null) {
                try {
                    // 注入ArticleLikesService来检查点赞状态
                    isLiked = articleLikesService.hasUserLiked(articleId, userId);
                } catch (Exception e) {
                    System.err.println("检查用户点赞状态失败: " + e.getMessage());
                    // 如果检查失败，默认为未点赞
                    isLiked = false;
                }
            }
            dataMap.put("isLiked", isLiked ? 1 : 0);

            // 获取上一篇文章信息
            Long lastArticleId = article.getLastArticleId();
            if (lastArticleId != null && lastArticleId != 0) {
                Article lastArticle = articleMapper.getArticleByArticleId(lastArticleId);
                if (lastArticle != null) {
                    dataMap.put("lastStatus", "200");
                    dataMap.put("lastArticleTitle", lastArticle.getArticleTitle());
                    dataMap.put("lastArticleUrl", "/article/" + lastArticleId);
                } else {
                    dataMap.put("lastStatus", "500");
                    dataMap.put("lastInfo", "没有上一篇了");
                }
            } else {
                dataMap.put("lastStatus", "500");
                dataMap.put("lastInfo", "没有上一篇了");
            }

            // 获取下一篇文章信息
            Long nextArticleId = article.getNextArticleId();
            if (nextArticleId != null && nextArticleId != 0) {
                Article nextArticle = articleMapper.getArticleByArticleId(nextArticleId);
                if (nextArticle != null) {
                    dataMap.put("nextStatus", "200");
                    dataMap.put("nextArticleTitle", nextArticle.getArticleTitle());
                    dataMap.put("nextArticleUrl", "/article/" + nextArticleId);
                } else {
                    dataMap.put("nextStatus", "500");
                    dataMap.put("nextInfo", "没有下一篇了");
                }
            } else {
                dataMap.put("nextStatus", "500");
                dataMap.put("nextInfo", "没有下一篇了");
            }

            return DataMap.success().setData(dataMap);

        } catch (Exception e) {
            System.err.println("获取文章详情失败: " + e.getMessage());
            e.printStackTrace();
            return DataMap.fail(CodeType.SERVER_EXCEPTION);
        }
    }

    @Override
    public DataMap getArticleManagement(int rows, int pageNum) {
        //开启分页插件
        PageHelper.startPage(pageNum, rows);
        //查询文章并且存入集合中
        List<Article> articleList = articleMapper.getArticleManagement();
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        //返回数据处理
        JSONArray returnJsonArray = new JSONArray();
        JSONObject returnJsonObject = new JSONObject();
        //创建一个articleJson
        JSONObject articleJson;
        for (Article article : articleList) {
            articleJson = new JSONObject();
            articleJson.put("id", article.getId());
            articleJson.put("articleId", article.getArticleId());
            articleJson.put("articleTitle", article.getArticleTitle());
            articleJson.put("publishDate", article.getPublishDate());
            articleJson.put("articleUrl", article.getArticleUrl());
            articleJson.put("articleCategories", article.getArticleCategories());
            articleJson.put("originalAuthor", article.getOriginalAuthor());
            articleJson.put("visitorNum", statisticsMapper.getvisitorNumByPageName("article/"+article.getArticleId()));//TODO 获取文章访问量
            returnJsonArray.add(articleJson);
        }
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
}
