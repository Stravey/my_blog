package com.liu.blog.dao;

import com.liu.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * User表接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-23
 */
@Mapper
public interface UserDao {

    /**
     * 根据用户名和密码查询
     * @param username 用户名
     * @param password 密码
     * @return user对象
     */
    User findOneUserByUsernameAndPassword(String username,String password);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return user对象
     */
    User findOneUserByUsername(String username);

    /**
     * 查找用户
     * @return user对象
     */
    User findOneUser();

}
