package com.wtx.myblog.service.impl;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.User;
import com.wtx.myblog.service.UserService;
import com.wtx.myblog.utils.DataMap;
import com.wtx.myblog.utils.StringCleanTrim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 26989
 * @date 2025/9/5
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;


    // 根据手机号查询用户
    @Override
    public User findUserByPhone(String phone) {
        User user = userMapper.findUserByPhone(phone);
        return user;
    }

    // 用户名是否存在
    @Override
    public boolean userNameIsExist(String username) {
        User user = userMapper.userNameIsExit(username);
        return user != null;
    }

    // 注册用户
    @Override
    @Transactional
    public DataMap insertUser(User user) {
        // 清洗字符串空格
        user.setUsername(StringCleanTrim.cleanTrim(user.getUsername()));
        // 判断用户是否异常：用户名长度，用户名特殊字符
        if (user.getUsername().length() > 35) {
            return DataMap.fail(CodeType.USERNAME_FORMAT_ERROR);
        }
        // 手机号是否存在
        User phoneIsExit = userMapper.phoneIsExit(user.getPhone());
        if (phoneIsExit != null) {
            return DataMap.fail(CodeType.PHONE_EXIST);
        }
        //
        if ("male".equals(user.getGender())) {
            user.setAvatarImgUrl("www.javatiaozao.com");
        } else {
            user.setAvatarImgUrl("www.javatiaozao.com");
        }
        userMapper.insertUser(user);
        // 注册成功，配置角色  默认：普通用户
        User userByPhone = userMapper.findUserByPhone(user.getPhone());
        updateRoleByUserId(userByPhone.getId(), 1);
        return DataMap.success();
    }

    @Override
    public DataMap getUserPersonalInfo(String username) {
        User user = userMapper.getUserPersonalInfo(username);
        return DataMap.success().setData(user);
    }

    @Override
    public int getUserIdByUserName(String userName) {
        int userId = userMapper.getUserIdByUserName(userName);
        return userId;
    }

    private void updateRoleByUserId(int id, int roleId) {
        userMapper.updateRoleByUserId(id, roleId);
    }

}
