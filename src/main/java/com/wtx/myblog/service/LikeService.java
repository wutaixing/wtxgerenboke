package com.wtx.myblog.service;

import com.wtx.myblog.model.FriendLink;
import com.wtx.myblog.utils.DataMap;

/**
 * @author 26989
 * @date 2025/9/17
 * @description
 */
public interface LikeService {
    DataMap getArticleThumbsUp(int rows, int pageNum);

    DataMap readAllThumbsUp();

    DataMap getFriendLink();

    DataMap addFriendLink(FriendLink friendLink);

    DataMap updateFriendLink(FriendLink friendLind, String id);

    DataMap deleteFriendLink(String id);
}
