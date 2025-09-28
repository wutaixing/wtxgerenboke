package com.wtx.myblog.service.impl;

import com.wtx.myblog.mapper.FriendLinkMapper;
import com.wtx.myblog.model.FriendLink;
import com.wtx.myblog.service.FriendLinkService;
import com.wtx.myblog.utils.DataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 26989
 * @date 2025/9/25
 * @description
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public DataMap getFriendLinkInfo() {

        List<FriendLink> friendLinkList = friendLinkMapper.getFriendLinkInfo();
        ArrayList<Object> arrayList = new ArrayList<>();
        HashMap<String, Object> map;
        for (FriendLink friendLink : friendLinkList) {
            map = new HashMap<>();
            map.put("blogger", friendLink.getBlogger());
            map.put("url", friendLink.getUrl());
            arrayList.add(map);
        }
        return DataMap.success().setData(arrayList);
    }
}
