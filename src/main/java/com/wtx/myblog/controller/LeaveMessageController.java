package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.service.LeaveMessageService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
@RestController
@Slf4j
public class LeaveMessageController {

    @Autowired
    private LeaveMessageService leaveMessageService;

    /**
     * @description: //发表留言
     * @param: request
     * @param: leaveMessageContent
     * @param: pageName
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/25 14:45
    */
    @PostMapping("/publishLeaveMessage")
    public String publishLeaveMessage(HttpServletRequest request, String leaveMessageContent, String pageName) {
        try {
            Principal userPrincipal = request.getUserPrincipal();
            if (userPrincipal == null) {
                return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
            }
            String username = userPrincipal.getName();
            DataMap data = leaveMessageService.publishLeaveMessage(username,leaveMessageContent,pageName);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LeaveMessageController】publishLeaveMessage Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }


    /**
     * @description: //获取留言列表
     * @param: request
     * @param: pageName
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/25 15:16
    */
    @GetMapping("/getPageLeaveMessage")
    public String getPageLeaveMessage(HttpServletRequest request,String pageName) {
        try {
            Principal userPrincipal = request.getUserPrincipal();
            String username;
            if (userPrincipal != null) {
                username = userPrincipal.getName();
            } else {
                username = "";
            }
            DataMap data = leaveMessageService.getPageLeaveMessage(pageName, username);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【LeaveMessageController】getPageLeaveMessage Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
