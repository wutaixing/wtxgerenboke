package com.wtx.myblog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.TagsMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.model.Role;
import com.wtx.myblog.model.vo.ArticleRequestVO;
import com.wtx.myblog.model.vo.IsReadResponseVO;
import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagsMapper tagsMapper;
    @Autowired
    private UserMapper userMapper;

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
            articleRequestVO.setReleaseStatus(1);// 发表 0 草稿 1 发表
            articleRequestVO.setLikes(0);

            // 1.插入文章
            int insertResult  = articleMapper.insertArticle(articleRequestVO, tagsString);
            int tagsResult  = tagsMapper.inserttags(articleRequestVO.getArticleTitle(), articleRequestVO.getArticleGrade());
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
    @Transactional(rollbackFor = Exception.class)
    public DataMap updateArticle(ArticleRequestVO articleRequestVO) {
        try {
            // 文章跟新
            // 根据文章的id更新  articleRequestVO.id == article.articleId

            // 获取article数据库中的 旧数据
            Article oldArticle = articleMapper.getArticleIdById(articleRequestVO.getId());
            if ("原创".equals(articleRequestVO.getArticleType())){
                oldArticle.setArticleUrl("https://www.qiuzhiwang.vip/article/"+String.valueOf(oldArticle.getArticleId()));
                oldArticle.setOriginalAuthor(oldArticle.getAuthor());
            }
            if ("转载".equals(articleRequestVO.getArticleType())){
                oldArticle.setArticleUrl(articleRequestVO.getArticleUrl());
                oldArticle.setOriginalAuthor(oldArticle.getAuthor());
            }
            //判断文章标题是否改变
            int result = 0;
            if (!articleRequestVO.getArticleTitle().equals(oldArticle.getArticleTitle())){
                result = tagsMapper.deleteTagsByArticleTitle(articleRequestVO.getArticleTitle());
            }
            oldArticle.setArticleTitle(articleRequestVO.getArticleTitle());
            oldArticle.setArticleContent(articleRequestVO.getArticleHtmlContent());
            //更新时间
            TimeUtil timeUtil = new TimeUtil();
            String formatDateForSix = timeUtil.getFormatDateForSix();
            oldArticle.setUpdateDate(formatDateForSix);
            //更新标签
            StringAndArray stringAndArray = new StringAndArray();
            String tagsString = stringAndArray.arrayToString(articleRequestVO.getArticleTagsValue());
            oldArticle.setArticleTags(tagsString);
            //更新文章内容
            BuildArticleTabloidUtil buildArticleTabloidUtil = new BuildArticleTabloidUtil();
            String buildArticleTabloid = buildArticleTabloidUtil.buildArticleTabloid(articleRequestVO.getArticleHtmlContent());
            oldArticle.setArticleTabloid(buildArticleTabloid);
            //更新文章状态
            oldArticle.setReleaseStatus(1);// 发表 0 草稿 1 发表

            // 1.更新文章
            int updatedResult  = articleMapper.updateArticleByIdAndArticle(oldArticle,oldArticle.getId());
            // 2.更新文章tags
            int tagsResult = 0;
            if (result > 0) { // 文章标题改变
                tagsResult  = tagsMapper.inserttags(oldArticle.getArticleTitle(), articleRequestVO.getArticleGrade());
            }else{
                tagsResult  = tagsMapper.updatetags(oldArticle.getArticleTitle(), articleRequestVO.getArticleGrade());
            }
            if (updatedResult  > 0 && tagsResult >0) {
                return DataMap.success(CodeType.SUCCESS_STATUS).message("更新文章成功").setData(oldArticle);
            }
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("更新文章失败");
        } catch (Exception e) {
            log.error("更新文章异常", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION);
        }
    }

    @Override
    public DataMap canYouWrite() {
        // 0：有权限  103：异常  ！=0 ：没有权限
        // 获取当前用户名
        String currentUsername = UserUtils.getCurrentUsername();
        // 判断用户是否登录
        if(Objects.isNull(currentUsername) || currentUsername.isEmpty()){
            return DataMap.fail(CodeType.USER_NOT_LOGIN);
        }
        // 查询用户角色
        List<Role> roles = userMapper.queryRolesBYUserName(currentUsername);
        // 判断用户是否有写博客权限
        // 例如：ROLE_SUPERADMIN和ROLE_ADMIN有权限
        boolean hasPermission = roles.stream().anyMatch(role -> "ROLE_SUPERADMIN".equals(role.getName()) || "ROLE_ADMIN".equals(role.getName()));
        if (hasPermission) {
            // 有权限
            return DataMap.success(CodeType.SUCCESS_STATUS).message("用户有写博客权限");
        } else {
            // 无权限
            return DataMap.fail(CodeType.PERMISSION_VERIFY_FAIL);
        }
    }

    @Override
    public DataMap getUserPersonalInfo() {
        String uname = UserUtils.getCurrentUsername();
        if(Objects.isNull(uname) || uname.isEmpty()){
            return DataMap.fail(CodeType.USER_NOT_LOGIN);
        }else {
            JSONObject jsondata = new JSONObject();
            jsondata.put("username",uname);
            return DataMap.success(CodeType.SUCCESS_STATUS).setData(jsondata);
        }
    }

    @Override
    public DataMap getMyArticles(Integer rows, Integer pageNum) {
        try {
            // 使用PageHelper进行分页
            PageHelper.startPage(pageNum, rows);
            List<Article> articleList = articleMapper.selectAllArticles();
            // 获取分页信息
            PageInfo<Article> pageInfo = new PageInfo<>(articleList);

            // 转换数据格式以适配前端
            List<Map<String, Object>> result = new ArrayList<>();
            for (Article article : articleList) {
                Map<String, Object> articleMap = new HashMap<>();
                articleMap.put("articleTitle", article.getArticleTitle());
                articleMap.put("articleType", article.getArticleType());
                articleMap.put("publishDate", article.getPublishDate());
                articleMap.put("originalAuthor", article.getOriginalAuthor());
                articleMap.put("articleCategories", article.getArticleCategories());
                articleMap.put("articleTabloid", article.getArticleTabloid());
                articleMap.put("thisArticleUrl", article.getArticleUrl()); // 注意字段名

                // 处理标签，从逗号分隔的字符串转换为数组
                String tagsStr = article.getArticleTags();
                if (tagsStr != null && !tagsStr.isEmpty()) {
                    articleMap.put("articleTags", tagsStr.split(","));
                } else {
                    articleMap.put("articleTags", new String[0]);
                }

                // 添加其他需要的字段
                articleMap.put("likes", article.getLikes());

                result.add(articleMap);
            }

            // 添加分页信息到结果数组的末尾（保持前端代码的期望）
            Map<String, Object> paginationInfo = new HashMap<>();
            paginationInfo.put("pageSize", pageInfo.getPageSize());
            paginationInfo.put("pageNum", pageInfo.getPageNum());
            paginationInfo.put("pages", pageInfo.getPages());
            paginationInfo.put("total", pageInfo.getTotal());
            result.add(paginationInfo);

            return DataMap.success(CodeType.SUCCESS_STATUS).message("获取文章列表成功").setData(result);
        } catch (Exception e) {
            log.error("获取文章列表异常", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("获取文章列表失败");
        }
    }

    @Override
    public DataMap getArticleThumbsUp(Integer rows, Integer pageNum) {
        // 判断用户登录
        if (UserUtils.getCurrentUsername().isEmpty() || Objects.isNull(UserUtils.getCurrentUsername())) {
            return DataMap.fail(CodeType.USER_NOT_LOGIN);
        }
        // 获取分页信息
        PageHelper.startPage(pageNum, rows);
        List<IsReadResponseVO> IsReadResponseVOs = articleMapper.getAllIsReadArticleByUserName(UserUtils.getCurrentUsername());
        PageInfo<IsReadResponseVO> pageInfo = new PageInfo<>(IsReadResponseVOs);

        // 获取未读数量
        int unreadCount = articleMapper.countUnreadLikesByAuthor(UserUtils.getCurrentUsername());

        // 转换为前端需要的数据格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (IsReadResponseVO isReadResponseVO : IsReadResponseVOs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", isReadResponseVO.getId());
            map.put("praisePeople", isReadResponseVO.getPraisePeople()); // 点赞人
            map.put("articleId", isReadResponseVO.getArticleId());
            map.put("articleTitle", isReadResponseVO.getArticleTitle());
            map.put("likeDate", isReadResponseVO.getLikeDate());
            map.put("isRead", isReadResponseVO.getIsRead()); // 1:未读, 0:已读
            result.add(map);
        }

        // 构造返回数据
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", result);
        responseData.put("msgIsNotReadNum", unreadCount);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("pageNum", pageInfo.getPageNum());
        pageInfoMap.put("pageSize", pageInfo.getPageSize());
        pageInfoMap.put("pages", pageInfo.getPages());
        pageInfoMap.put("total", pageInfo.getTotal());
        responseData.put("pageInfo", pageInfoMap);

        return DataMap.success().setData(responseData);
    }

    @Override
    public DataMap markLikeAsRead(Integer id) {
        try {
            int result = articleMapper.markLikeAsRead(id);
            if (result > 0) {
                return DataMap.success();
            } else {
                return DataMap.fail(CodeType.SERVER_EXCEPTION).message("标记已读失败");
            }
        } catch (Exception e) {
            log.error("标记点赞为已读失败", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("标记已读失败");
        }
    }

    @Override
    public DataMap markAllLikesAsRead(String author) {
        try {
            articleMapper.markAllLikesAsRead(author);
            return DataMap.success();
        } catch (Exception e) {
            log.error("标记所有点赞为已读失败", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("标记已读失败");
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
            // 先获取要删除的文章信息，用于后续更新关联文章的链表关系
            Article articleToDelete = articleMapper.getArticleIdById(Long.valueOf(id));
            if (articleToDelete == null) {
                return DataMap.fail(CodeType.ARTICLE_NOT_EXIST).message("文章不存在");
            }
            // 执行删除操作
            int result = articleMapper.deleteArticleByid(id);
            if (result <= 0) {
                return DataMap.fail(CodeType.SERVER_EXCEPTION).message("删除文章失败");
            }
            // 获取被删除文章的上一篇文章和下一篇文章ID
            Long lastArticleId = articleToDelete.getLastArticleId();
            Long nextArticleId = articleToDelete.getNextArticleId();
            // 更新上一篇文章的 nextArticleId
            if (lastArticleId != null) {
                articleMapper.updateNextArticleLastPointer(nextArticleId,lastArticleId);
            }
            // 更新下一篇文章的 lastArticleId
            if (nextArticleId != null) {
                articleMapper.updateLastArticleNextPointer(lastArticleId,nextArticleId);
            }
            return DataMap.success(CodeType.SUCCESS_STATUS).message("删除文章成功");
        } catch (Exception e) {
            log.error("删除文章异常", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("删除文章失败");
        }
    }

    @Override
    public DataMap getDraftArticle(Integer id) {
        Article article = articleMapper.getDraftArticleById(id);
        if(Objects.nonNull(article)){
            int result = articleMapper.setArticleReleaseStatusById(article.getId(), 0);
            if(result > 0){
                Article newArticle = articleMapper.getDraftArticleById(id);
                // 获取文章的等级
                int articleGrade = tagsMapper.findTagsByArticleTitle(newArticle.getArticleTitle());
                // 将文章标签字符串转换为数组
                String[] articleTags =StringAndArray.stringToArray(newArticle.getArticleTags());
                JSONObject resultJSON = new JSONObject();
                resultJSON.put("id", article.getId());
                resultJSON.put("articleTitle", article.getArticleTitle());
                resultJSON.put("articleContent", article.getArticleContent());
                resultJSON.put("articleType", article.getArticleType());
                resultJSON.put("articleCategories", article.getArticleCategories());
                resultJSON.put("articleGrade", articleGrade);
                resultJSON.put("originalAuthor", article.getOriginalAuthor());
                resultJSON.put("articleUrl", article.getArticleUrl());
                resultJSON.put("articleTags", articleTags);
                resultJSON.put("publishDate", article.getPublishDate());
                resultJSON.put("updateDate", article.getUpdateDate());
                return DataMap.success(CodeType.SUCCESS_STATUS).setData(resultJSON).message("获取草稿文章成功");
            }
        }
        return DataMap.fail(CodeType.SERVER_EXCEPTION);
    }


    @Override
    public DataMap getDraftArticle() {
        String username = UserUtils.getCurrentUsername();
        Article articleOne = articleMapper.getDraftArticleByUserName(username);
        //判断是否有草稿
        if(Objects.nonNull(articleOne)){
            //获取文章等级
            int articleGrade = tagsMapper.findTagsByArticleTitle(articleOne.getArticleTitle());
            // 将文章标签字符串转换为数组
            String[] articleTags =StringAndArray.stringToArray(articleOne.getArticleTags());
            //创建返回JSON对象
            JSONObject resultJSON = new JSONObject();
            resultJSON.put("id", articleOne.getId());
            resultJSON.put("articleTitle", articleOne.getArticleTitle());
            resultJSON.put("articleContent", articleOne.getArticleContent());
            resultJSON.put("articleType", articleOne.getArticleType());
            resultJSON.put("articleCategories", articleOne.getArticleCategories());
            resultJSON.put("articleGrade", articleGrade);
            resultJSON.put("originalAuthor", articleOne.getOriginalAuthor());
            resultJSON.put("articleUrl", articleOne.getArticleUrl());
            resultJSON.put("articleTags", articleTags);
            resultJSON.put("publishDate", articleOne.getPublishDate());
            resultJSON.put("updateDate", articleOne.getUpdateDate());
            return DataMap.success(CodeType.SUCCESS_STATUS).setData(resultJSON).message("获取草稿文章成功");
        }else {
            return DataMap.success(CodeType.SUCCESS_STATUS).message("用户没有草稿");
        }
    }
}