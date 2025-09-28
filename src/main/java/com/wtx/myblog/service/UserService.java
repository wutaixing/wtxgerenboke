package com.wtx.myblog.service;

import com.wtx.myblog.model.User;
import com.wtx.myblog.utils.DataMap;

/**
 * @author 26989
 * @date 2025/9/5
 * @description
 */
public interface UserService {

    // 根据手机号查询用户
    User findUserByPhone(String phone);

    // 判断用户名是否存在
    boolean userNameIsExist(String username);
    // 注册用户
    DataMap insertUser(User user);

    DataMap getUserPersonalInfo(String username);

    int getUserIdByUserName(String userName);
}
