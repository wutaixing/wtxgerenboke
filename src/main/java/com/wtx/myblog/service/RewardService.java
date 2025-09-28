package com.wtx.myblog.service;

import com.wtx.myblog.model.Reward;
import com.wtx.myblog.utils.DataMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author 26989
 * @date 2025/9/24
 * @description
 */
public interface RewardService {
    DataMap getRewardInfo(HttpServletRequest request);

    DataMap addReward(Reward reward, MultipartFile file, HttpServletRequest request);
    
    DataMap deleteReward(int id);
}
