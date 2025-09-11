package com.wtx.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.model.vo.ArticleRequestVO;
import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataMap insertArticle(ArticleRequestVO articleRequestVO) {
        try {
            TimeUtil timeUtil = new TimeUtil();
            String formatDateForSix = timeUtil.getFormatDateForSix();
            articleRequestVO.setPublishDate(formatDateForSix);
            articleRequestVO.setUpdateDate(formatDateForSix);
            // 生成文章ID
            long articleId = System.currentTimeMillis();
            articleRequestVO.setId(articleId);

            if ("原创".equals(articleRequestVO.getArticleType())){
                articleRequestVO.setArticleUrl("https://www.qiuzhiwang.vip/article/"+String.valueOf(articleId));
                articleRequestVO.setAuthor(UserUtils.getCurrentUsername());
                articleRequestVO.setOriginalAuthor(UserUtils.getCurrentUsername());
            }
            if ("转载".equals(articleRequestVO.getArticleType())){
                articleRequestVO.setAuthor(UserUtils.getCurrentUsername());
                articleRequestVO.setArticleUrl(articleRequestVO.getArticleUrl());
            }

            StringAndArray stringAndArray = new StringAndArray();
            String tagsString = stringAndArray.arrayToString(articleRequestVO.getArticleTagsValue());

            BuildArticleTabloidUtil buildArticleTabloidUtil = new BuildArticleTabloidUtil();
            String buildArticleTabloid = buildArticleTabloidUtil.buildArticleTabloid(articleRequestVO.getArticleHtmlContent());
            articleRequestVO.setArticleHtmlContent(buildArticleTabloid);

            articleRequestVO.setLikes(0);

            // 1.插入文章
            int insertResult  = articleMapper.insertArticle(articleRequestVO, tagsString);
            int tagsResult  = articleMapper.inserttags(articleRequestVO.getArticleTitle(), articleRequestVO.getArticleGrade());
            if (insertResult  > 0 && tagsResult >0) {
                // 上一条文章id
                Long currentArticleId  = articleRequestVO.getId();
                log.info("当前文章id:{}",currentArticleId);
                // 2. 查询上一篇文章（发布时间早于当前文章的最新文章）
                Long lastArticleId = articleMapper.findLatestArticleBefore(currentArticleId);
                log.info("上一篇文章id:{}",lastArticleId);
                // 下一条文章id
                // 3. 查询下一篇文章（发布时间晚于当前文章的最早文章）
                Long nextArticleId = articleMapper.findEarliestArticleAfter(currentArticleId);
                log.info("下一篇文章id:{}",nextArticleId);

                // 4. 更新当前文章的上下关系
                articleMapper.updateArticleRelations(currentArticleId, lastArticleId, nextArticleId);

                // 5. 更新相关文章的上下关系
                if (Objects.nonNull(lastArticleId)) {
                    // 更新上一篇文章的nextArticleId指向当前文章
                    articleMapper.updateLastArticleNextPointer(lastArticleId, currentArticleId);
                }
                if (Objects.nonNull(nextArticleId)) {
                    // 更新下一篇文章的lastArticleId指向当前文章
                    articleMapper.updateNextArticleLastPointer(nextArticleId, currentArticleId);
                }
                return DataMap.success(CodeType.SUCCESS_STATUS).message("发表文章成功").setData(articleRequestVO);
            }else{
                return DataMap.fail(CodeType.SERVER_EXCEPTION).message("发表文章失败");
            }
        } catch (Exception e) {
            log.error("发表文章异常", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION);
        }
    }

    @Override
    public DataMap getAllArticle(Integer rows, Integer pageNum) { //row: 每页显示的行数，pageNum: 当前页码
        try {

            // 使用PageHelper进行分页
            PageHelper.startPage(pageNum, rows);
            List<Article> articleList = articleMapper.selectAllArticles();
            // 获取分页信息
            PageInfo<Article> pageInfo = new PageInfo<>(articleList);
            HashMap<String, Object> pageInfoData = new HashMap<>();
            pageInfoData.put("pageNum", pageInfo.getPageNum());
            pageInfoData.put("pageSize", pageInfo.getPageSize());
            pageInfoData.put("pages", pageInfo.getPages());
            pageInfoData.put("total", pageInfo.getTotal());

            // 创建一个包含result键的对象，以匹配前端期望的数据结构
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", articleList);
            resultMap.put("pageInfo", pageInfoData);

            return DataMap.success(CodeType.SUCCESS_STATUS).message("获取文章列表成功").setData(resultMap);
        } catch (Exception e) {
            log.error("获取文章列表异常", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("获取文章列表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataMap deleteArticleByid(Integer id) {
        try {
            int result = articleMapper.deleteArticleByid(id);
            if (result <= 0) {
                return DataMap.fail(CodeType.SERVER_EXCEPTION).message("删除文章失败");
            }
            return DataMap.success(CodeType.SUCCESS_STATUS).message("删除文章成功");
        } catch (Exception e) {
            log.error("获取文章列表异常", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("获取文章列表失败");
        }
    }
}
