package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.model.Article;
import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.service.LikeService;
import com.wtx.myblog.service.TagService;
import com.wtx.myblog.service.UserService;
import com.wtx.myblog.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

/**
 * @author 26989
 * @date 2025/9/11
 * @description 博客文章模块控制类
 */
@RestController
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    /**
     * @description: //TODO 获取文章草稿或者修改文章内容接口
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 14:15
     */
    @GetMapping("/getDraftArticle")
    public String getDraftArticle(HttpServletRequest request) {
        try {
            String id = (String) request.getSession().getAttribute("id");
            // 判断文章是否是修改
            if (id != null) {
                Article article = articleService.getArticleById(id);
                int lastIndexOf = article.getArticleTags().lastIndexOf(",");
                // tags只有一个值，没有,分割，这个时候需要判断
                if(lastIndexOf != -1){
                    String[] tagStr = StringAndArray.stringToArray(article.getArticleTags().substring(0, lastIndexOf));
                    DataMap dataMap = articleService.getDraftArticle(article, tagStr, tagService.getTagSizeByName(tagStr[0]));
                    return JsonResult.build(dataMap).toJSON();
                }
                String[] tagStr = StringAndArray.stringToArray(article.getArticleTags());
                DataMap dataMap = articleService.getDraftArticle(article, tagStr, tagService.getTagSizeByName(tagStr[0]));
                return JsonResult.build(dataMap).toJSON();
            }
            //判断是否写文章登录超时
            if (request.getSession().getAttribute("article") != null) {
                Article article = (Article) request.getSession().getAttribute("article");
                String[] tagStr = (String[]) request.getSession().getAttribute("articleTags");
                String articleGrade = (String) request.getSession().getAttribute("articleGrade");
                if (!StringUtil.BLANK.equals(1) && articleGrade != null) {
                    DataMap dataMap = articleService.getDraftArticle(article, tagStr, Integer.valueOf(articleGrade));
                    request.getSession().removeAttribute("article");
                    request.getSession().removeAttribute("articleTags");
                    request.getSession().removeAttribute("articleGrade");
                    return JsonResult.build(dataMap).toJSON();
                }
            }
            return JsonResult.fail().toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】getArticleManagement Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 验证当前用户是否有编辑或者发布文章权限
     * @param: principal
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 15:12
     */
    @GetMapping("/canYouWrite")
    public String canYouWrite(@AuthenticationPrincipal Principal principal,HttpServletRequest  request) {
        try {
            Principal userPrincipal = request.getUserPrincipal();
            if(!Objects.isNull(userPrincipal)){
                String username = userPrincipal.getName();
                boolean b = userService.userNameIsExist(username);
                if(b){//如果是true，当前用户可以编辑文章
                    return JsonResult.success().toJSON();
                }
                return JsonResult.fail().toJSON();
            }
            return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】deleteArticle Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 删除文章接口
     * @param: id
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 14:14
     */
    @GetMapping("/deleteArticle")
    public String deleteArticle(@RequestParam("id") String id) {
        try {
            if (StringUtil.BLANK.equals(id) || id == null) {
                return JsonResult.fail(CodeType.DELETE_ARTICLE_FAIL).toJSON();
            }
            DataMap data = articleService.deleteArticle(id);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】deleteArticle Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 获取文章管理数据接口
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 14:13
     */
    @PostMapping("/getArticleManagement")
    public String getArticleManagement(@RequestParam(value = "rows") int rows, @RequestParam(value = "pageNum") int pageNum) {
        try {
            DataMap data = articleService.getArticleManagement(rows, pageNum);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】getArticleManagement Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 获取首页文章列表
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/17 17:09
    */
    @PostMapping("/myArticles")
    public String getMyArticles(@RequestParam(value = "rows") int rows, @RequestParam(value = "pageNum") int pageNum) {
        try {
            DataMap data = articleService.getMyArticles(rows, pageNum);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】getArticleManagement Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 发博客功能接口
     * @param: articleRequestVO
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/11 18:09
     */
    @PostMapping("/publishArticle")
    //如果前端发送的是 JSON 数据，需要添加 @RequestBody 注解来接收。如果是表单数据（application/x-www-form-urlencoded），则不需要注解，Spring 会自动绑定参数。
    public String publishArticle(Article article, @RequestParam("articleGrade") String articleGrade,
                                 @AuthenticationPrincipal Principal principal, HttpServletRequest request) {
        try {
            // 获取文章作者（当前登录的用户名）
            //String name = "奋斗着";
            Principal userPrincipal = request.getUserPrincipal();
            String name = userPrincipal.getName();
            //获取html代码中生成的文章摘要，根据关键字”articleHtmlContent“
            //赋值到文章对应的articleTabloid字段
            BuildArticleTabloidUtil buildArticleTabloidUtil = new BuildArticleTabloidUtil();
            String articleHtmlContent = buildArticleTabloidUtil.buildArticleTabloid(request.getParameter("articleHtmlContent"));
            article.setArticleTabloid(articleHtmlContent + "...");
            //处理前端传递过来的标签，比如对些特殊关键字、空格进行处理
            //插入对应的标签实体类
            //String articleTags = request.getParameter("articleTags");//需要的是一个字符串数组，所以需要处理成字符串数组
            String[] articleTags = request.getParameterValues("articleTagsValue");
            String[] newArticleTAgs = new String[articleTags.length + 1];
            for (int i = 0; i < articleTags.length; i++) {
                //去除关键字，空格进行处理
                newArticleTAgs[i] = articleTags[i].replaceAll("<br>", StringUtil.BLANK).replaceAll("&nbsp;", StringUtil.BLANK)
                        .replaceAll("\\s", StringUtil.BLANK).trim();
            }
            newArticleTAgs[articleTags.length] = article.getArticleType();
            //插入对应的标签实体类
            tagService.insertTags(newArticleTAgs, Integer.parseInt(articleGrade));
            //通过是否有文章id判断当前接口是处理新增文章还是修改文章
            //有id 为修改文章，无id 为新增文章
            String id = request.getParameter("id");
            if (!StringUtil.BLANK.equals(id) && id != null) {
                //是修改文章
                TimeUtil timeUtil = new TimeUtil();
                String updateDate = timeUtil.getFormatDateForThree();
                article.setArticleTags(StringAndArray.arrayToString((newArticleTAgs)));//将数组转成string
                article.setUpdateDate(updateDate);
                article.setId(Integer.parseInt(id));
                article.setAuthor(name);
                DataMap data = articleService.updateArticle(article);
                return JsonResult.build(data).toJSON();
            }
            //否则就是新增文章
            TimeUtil timeUtil = new TimeUtil();
            String formatDateForThree = timeUtil.getFormatDateForThree();
            long articleId = timeUtil.getLongTime();
            //封装实体类
            article.setArticleId(articleId);
            article.setAuthor(name);
            article.setArticleTags(StringAndArray.arrayToString((newArticleTAgs)));//将数组转成string
            article.setPublishDate(formatDateForThree);
            article.setUpdateDate(formatDateForThree);
            DataMap data = articleService.insertArticle(article);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【ArticleController】publishArticle异常！", e);
        }
        // 失败
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
