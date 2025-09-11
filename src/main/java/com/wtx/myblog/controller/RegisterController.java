package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.model.User;
import com.wtx.myblog.model.info.RegisterInfo;
import com.wtx.myblog.service.UserService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import com.wtx.myblog.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 26989
 * @date 2025/9/6
 * @description 注册
 */
@RestController
@Slf4j
public class RegisterController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(User user, HttpServletRequest request) {
        try {
            // 处理控制层数据
            // 判断手机号是否注册
            if(userService.userNameIsExist(user.getUsername())){
                return JsonResult.fail(CodeType.USERNAME_EXIST).toJSON();
            }
            // 对密码加密
            MD5Util md5Util = new MD5Util();
            user.setPassword(md5Util.encode(user.getPassword()));
            // 注册
            DataMap data = userService.insertUser(user);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【注册】发生异常！",user, e);

        }
        // 失败
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

}
