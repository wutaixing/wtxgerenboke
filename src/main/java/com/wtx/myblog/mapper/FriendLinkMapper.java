package com.wtx.myblog.mapper;

import com.wtx.myblog.model.FriendLink;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
@Mapper
@Repository
public interface FriendLinkMapper {
    List<FriendLink> getFriendLinkInfo();
}
