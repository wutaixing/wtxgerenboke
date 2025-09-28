package com.wtx.myblog.controller;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.model.Reward;
import com.wtx.myblog.service.RewardService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author 26989
 * @date 2025/9/24
 * @description
 */
@RestController
@Slf4j
public class RewardController {

    @Autowired
    private RewardService rewardService;

    /**
     * @description: //获取募捐管理信息
     * @param: request
     * @return: java.lang.String
     * @author 26989
     * @date: 2025/9/24 11:16
    */
    @PostMapping("/getRewardInfo")
    public String getUserNews(HttpServletRequest request) {
        try {
            DataMap data = rewardService.getRewardInfo(request);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【RewardController】getRewardInfo Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    @PostMapping("/addReward")
    public String addReward(Reward reward, @RequestParam("file") MultipartFile file,
                            HttpServletRequest request) {
        try {
            DataMap data = rewardService.addReward(reward,file, request);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【RewardController】addReward Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
    
    /**
     * 删除募捐记录
     * @param id 募捐记录ID
     * @return 删除结果
     */
    @GetMapping("/deleteReward")
    public String deleteReward(@RequestParam("id") int id) {
        try {
            DataMap data = rewardService.deleteReward(id);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("【RewardController】deleteReward Exception", e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}

