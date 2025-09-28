package com.wtx.myblog.service.impl;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.RewardMapper;
import com.wtx.myblog.model.Reward;
import com.wtx.myblog.service.RewardService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.FileUtil;
import com.wtx.myblog.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author 26989
 * @date 2025/9/24
 * @description
 */
@Service
@Slf4j
public class RewardServiceImpl implements RewardService {

    @Value("${file.upload-dir}")  // 在application.yml中配置
    private String uploadDir;
    @Autowired
    private RewardMapper rewardMapper;
    
    @Override
    public DataMap deleteReward(int id) {
        // 先获取募捐记录，用于删除对应的图片文件
        Reward reward = rewardMapper.getRewardById(id);
        if (reward == null) {
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("未找到指定的募捐记录");
        }
        
        // 删除数据库记录
        int rows = rewardMapper.deleteReward(id);
        if (rows > 0) {
            // 删除对应的图片文件
            deleteRewardImage(reward.getRewardUrl());
            
            return DataMap.success(CodeType.DELETE_REWARD_SUCCESS).setData(reward.getRewardMoney());
        } else {
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("删除失败");
        }
    }
    
    /**
     * 删除募捐图片文件
     * @param rewardUrl 图片URL路径
     */
    private void deleteRewardImage(String rewardUrl) {
        if (rewardUrl != null && !rewardUrl.isEmpty()) {
            try {
                // 处理上传目录配置
                String dir = uploadDir;
                if (dir == null || dir.isEmpty()) {
                    // 如果没有配置或配置为空，使用默认路径
                    dir = "./src/main/resources/static/upload";
                }
                
                // 规范化路径
                File directory = new File(dir).getAbsoluteFile();
                
                // rewardUrl格式为 /upload/reward/文件名，需要去掉前缀/upload
                String relativePath = rewardUrl;
                if (rewardUrl.startsWith("/upload/")) {
                    relativePath = rewardUrl.substring(7); // 去掉前缀"/upload"
                }
                
                // 构造文件完整路径
                File file = new File(directory, relativePath);
                
                // 如果文件存在则删除
                if (file.exists() && file.isFile()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        log.warn("删除募捐图片失败: " + file.getAbsolutePath());
                    }
                } else {
                    log.warn("募捐图片不存在: " + file.getAbsolutePath());
                }
            } catch (Exception e) {
                log.error("删除募捐图片异常: ", e);
            }
        }
    }
    
    @Override
    public DataMap getRewardInfo(HttpServletRequest request) {
        List<Reward> rewardList = rewardMapper.getAllReward(request);
        return DataMap.success().setData(rewardList);
    }

    @Override
    public DataMap addReward(Reward reward,MultipartFile file, HttpServletRequest request) {
        try {
            //获取募捐时间
            String rewardDate = request.getParameter("reward-date");
            
            // 初始化文件URL为空字符串而不是null
            String fileUrl = "";
            TimeUtil timeUtil = new TimeUtil();
            //上传附件
            if (file != null && !file.isEmpty()) {
                FileUtil fileUtil = new FileUtil();
                String filePath = this.getClass().getResource("/").getPath().substring(1)+"blogImg/";
                String contentType = file.getContentType();
                String fileEx = contentType.substring(contentType.lastIndexOf("/") + 1);
                String fileName = timeUtil.getLongTime() + "." + fileEx;
                String fileCatalog = "rewardRecord" + timeUtil.getFormatDateForThree();
                fileUrl = fileUtil.uploadFile(fileUtil.multipartFileToFile(file, filePath, fileName),fileCatalog);
            }
            
            //处理募捐记录
            reward.setRewardDate(timeUtil.stringToDateThree(rewardDate));
            if (fileUrl != null && !fileUrl.isEmpty()){
                reward.setRewardUrl(fileUrl);
            }else {
                reward.setRewardUrl("....");// 即使没有上传文件，也设置为空字符串而不是null
            }
            reward.setFundRaiser("IT枫斗者");
            reward.setFundraisingPlace("1");
            reward.setFundRaisingSources("2");
            rewardMapper.insertReward(reward);
            return DataMap.success(CodeType.ADD_REWARD_SUCCESS).setData(reward.getId());
        } catch (Exception e) {
            log.error("【RewardServiceImpl】addReward Exception", e);
            return DataMap.fail(CodeType.SERVER_EXCEPTION).message("添加失败");
        }
    }

    /**
     * 保存图片文件
     */
    private String saveRewardImage(MultipartFile file) throws IOException {
        // 处理上传目录配置
        String dir = uploadDir;
        if (dir == null || dir.isEmpty()) {
            // 如果没有配置或配置为空，使用默认路径
            dir = "./src/main/resources/static/upload";
        }
        
        // 规范化路径
        File directory = new File(dir).getAbsoluteFile();
        File rewardDir = new File(directory, "reward");
        
        // 创建上传目录
        if (!rewardDir.exists()) {
            boolean created = rewardDir.mkdirs();
            if (!created) {
                throw new IOException("无法创建目录: " + rewardDir.getAbsolutePath());
            }
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + fileExtension;

        // 保存文件
        File dest = new File(rewardDir, fileName);
        file.transferTo(dest);

        // 返回相对路径，用于Web访问
        return "/upload/reward/" + fileName;
    }
}
