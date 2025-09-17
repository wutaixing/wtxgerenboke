package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Role;
import com.wtx.myblog.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/6
 * @description
 */
@Mapper
@Repository
public interface UserMapper {

    // 根据手机号查询用户
    User findUserByPhone(@Param("phone") String phone);
    // 检查用户名是否存在
    User userNameIsExit(@Param("username") String username);
    // 检查手机号是否存在
    User phoneIsExit(@Param("phone") String phone);

    void insertUser(User user);

    void updateRecentlyLanded(@Param("phone") String phone,@Param("recentlyLanded") String formatDateForSix);

    List<Role> queryRolesBYPhone(String phone);

    void updateRoleByUserId(@Param("userId") int userId,@Param("roleId") int roleId);


    User getUserPersonalInfo(String username);

    String getUserNameById(int id);
}
