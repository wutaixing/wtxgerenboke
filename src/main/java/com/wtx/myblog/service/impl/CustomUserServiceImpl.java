package com.wtx.myblog.service.impl;

import com.wtx.myblog.constant.CodeType;
import com.wtx.myblog.mapper.UserMapper;
import com.wtx.myblog.model.Role;
import com.wtx.myblog.model.User;
import com.wtx.myblog.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 26989
 * @date 2025/9/6
 * @description
 */
@Service
public class CustomUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper UserMapper;
    @Qualifier("userMapper")
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        // 查询用户信息
        User user = UserMapper.findUserByPhone(phone);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(CodeType.USERNAME_NOT_EXIST.name());
        }
        // 当前用户不为空的时候，查询用户角色（ROLE_USER/ROLE_ADMIN/ROLE_SUPERADMIN）
        List<Role> roles = userMapper.queryRolesBYPhone(phone);
        user.setRoles(roles);
        // 处理记录用户登录系统的时间
        TimeUtil timeUtil = new TimeUtil();
        String formatDateForSix = timeUtil.getFormatDateForSix();
        // 跟新用户登录时间
        UserMapper.updateRecentlyLanded(phone, formatDateForSix);
        // 添加权限
        ArrayList<SimpleGrantedAuthority> auths = new ArrayList<>();
        for (Role role : user.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auths);
    }
}
