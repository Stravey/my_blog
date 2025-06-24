package com.liu.blog.service;


import com.liu.blog.entity.User;

/**
 * User服务层接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
public interface UserService {

    User checkUser(String username, String password);

    User findUserByUsername(String username);

}
