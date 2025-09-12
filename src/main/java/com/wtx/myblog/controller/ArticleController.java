package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.Role;
import com.wtx.myblog.model.vo.ArticleRequestVO;
import com.wtx.myblog.service.ArticleService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import com.wtx.myblog.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author 26989
 * @date 2025/9/11
 * @description 博客文章控制类
 */
@RestController
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * @description: //TODO 发博客功能接口
     * @param: articleRequestVO
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/11 18:09
    */
    @PostMapping("/publishArticle")
    //如果前端发送的是 JSON 数据，需要添加 @RequestBody 注解来接收。如果是表单数据（application/x-www-form-urlencoded），则不需要注解，Spring 会自动绑定参数。
    public String publishArticle(ArticleRequestVO articleRequestVO) {
        try {
            // 获取当前用户名
            String currentUsername = UserUtils.getCurrentUsername();
            // 判断用户是否登录
            if(Objects.isNull(currentUsername) || currentUsername.isEmpty()){
                return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
            }
            // 查询用户角色
            List<Role> roles = userMapper.queryRolesBYUserName(currentUsername);
            // 判断用户是否有写博客权限   例如：ROLE_SUPERADMIN和ROLE_ADMIN有权限
            boolean hasPermission = roles.stream().anyMatch(role -> "ROLE_SUPERADMIN".equals(role.getName()) || "ROLE_ADMIN".equals(role.getName()));
            if (hasPermission){
                DataMap dataMap = null;
                //判断文章存在？
                if (articleMapper.findArticleByArticleId(articleRequestVO.getId())) {
                    //文章存在则更新
                    dataMap = articleService.updateArticle(articleRequestVO);
                }else{
                    // 文章不存在则创建新文章
                    dataMap = articleService.insertArticle(articleRequestVO);
                }
                return JsonResult.build(dataMap).toJSON();
            }
            return JsonResult.fail(CodeType.PUBLISH_ARTICLE_NO_PERMISSION).toJSON();
        } catch (Exception e) {
            log.error("【/publishArticle】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }


    /**
     * @description: //TODO 获得草稿文章接口
     * @param: id 文章id
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 15:07
     */
    @GetMapping("/getDraftArticle")
    public String getDraftArticle(@RequestParam(value = "id", required = false) String id) {
        try {
            DataMap dataMap = null;
            if(Objects.nonNull(id) && !id.isEmpty()){
                dataMap = articleService.getDraftArticle(Integer.parseInt(id));
            }else{
                dataMap = articleService.getDraftArticle();
            }
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/getDraftArticle】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 保存草稿文章接口
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 15:31
    */
    /*@GetMapping("/saveDraftArticle")
    public String getDraftArticle() {
        try {
            DataMap dataMap = articleService.saveDraftArticle();
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/getDraftArticle】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }*/

    /**
     * @description: //TODO 文章管理：获得文章分分页接口
     * @param: rows
     * @param: pageNum 
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 0:26
    */
    @PostMapping("/getArticleManagement")
    public String getArticleManagement(Integer rows,Integer pageNum) {
        try {
            DataMap dataMap = articleService.getAllArticle(rows, pageNum);
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/getArticleManagement】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 删除文章接口
     * @param: id
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 0:26
    */
    @GetMapping("/deleteArticle")
    public String deleteArticle(Integer id) {
        try {
            DataMap dataMap = articleService.deleteArticleByid(id);
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/deleteArticle】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 权限写博客接口
     * @param: username
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 16:01
    */
    @GetMapping("/canYouWrite")
    public String canYouWrite() {
        try {
            DataMap dataMap = articleService.canYouWrite();
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/canYouWrite】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 获得用户个人信息接口
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 17:02
    */
    @PostMapping("/getUserPersonalInfo")
    public String getUserPersonalInfo() {
        try {
            DataMap dataMap = articleService.getUserPersonalInfo();
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/getUserPersonalInfo】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 首页：文章分页接口
     * @param: rows 显示行数
     * @param: pageNum 页码
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 17:54
    */
    @PostMapping("/myArticles")
    public String myArticles(Integer rows,Integer pageNum) {
        try {
            DataMap dataMap = articleService.getMyArticles(rows, pageNum);
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/myArticles】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 获得文章点赞信息接口
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 18:35
    */
    @PostMapping("/getArticleThumbsUp")
    public String getArticleThumbsUp(Integer rows,Integer pageNum) {
        try {
            DataMap dataMap = articleService.getArticleThumbsUp(rows, pageNum);
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("【/getArticleThumbsUp】发生异常！", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 标记点赞为已读接口
     * @param: id
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 18:35
    */
    @GetMapping("/readThisThumbsUp")
    public String readThisThumbsUp(@RequestParam Integer id) {
        try {
            String author = UserUtils.getCurrentUsername();
            if (author == null) {
                return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
            }
            DataMap dataMap = articleService.markLikeAsRead(id);
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("标记点赞为已读异常", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

    /**
     * @description: //TODO 批量标记点赞为已读接口
     * @param:
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/12 18:35
    */
    @GetMapping("/readAllThumbsUp")
    public String readAllThumbsUp() {
        try {
            String author = UserUtils.getCurrentUsername();
            if (author == null) {
                return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
            }

            DataMap dataMap = articleService.markAllLikesAsRead(author);
            return JsonResult.build(dataMap).toJSON();
        } catch (Exception e) {
            log.error("标记所有点赞为已读异常", e);
            return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
        }
    }

}