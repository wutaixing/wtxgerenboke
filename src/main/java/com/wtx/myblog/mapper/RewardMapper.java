package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/24
 * @description
 */
@Mapper
@Repository
public interface RewardMapper {
    List<Reward> getAllReward(HttpServletRequest request);

    int insertReward(Reward reward);
    
    Reward getRewardById(int id);
    
    int deleteReward(int id);
}
