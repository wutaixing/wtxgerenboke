package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;

import com.wtx.myblog.service.FeedbackService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 26989
 * @date 2025/9/21
 * @description
 */
@RestController
@Slf4j
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * @description: //TODO分页获取反馈列表
     * @param: rows
     * @param: pageNum
     * @return: java.lang.String
     * @author 26989
     */
    @GetMapping("/getAllFeedback")
    public String getAllFeedback(@RequestParam(value = "rows") int rows, @RequestParam(value = "pageNum") int pageNum) {
        try {
            DataMap data = feedbackService.getAllFeedback(rows, pageNum);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【FeedbackController】getAllFeedback Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * @description: //TODO 查询所有悄悄话
     * @param:
     * @return: java.lang.String
     * @author 26989
    */
    @PostMapping("/getAllPrivateWord")
    public String getAllPrivateWord() {
        try {
            DataMap data = feedbackService.getAllPrivateWord();
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【FeedbackController】getAllFeedback Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
