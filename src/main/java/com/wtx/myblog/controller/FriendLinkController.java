package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.service.FriendLinkService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
@RestController
@Slf4j
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;
    /**
     * @description: // 获取友链信息
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/25 18:06
    */
    @PostMapping("/getFriendLinkInfo")
    public String newLeaveWord(HttpServletRequest request) {
        try {
            DataMap data = friendLinkService.getFriendLinkInfo();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【FriendLinkController】getFriendLinkInfo Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }


}
