package com.wtx.myblog.service.impl;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.ArticleMapper;
import com.wtx.myblog.mapper.StatisticsMapper;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.Visitor;
import com.wtx.myblog.service.StatisticsService;
import com.wtx.myblog.utils.DataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 26989
 * @date 2025/9/21
 * @description 统计服务实现类 - Redis+MySQL双重存储
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

}
