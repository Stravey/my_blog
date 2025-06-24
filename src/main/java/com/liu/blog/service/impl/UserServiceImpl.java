package com.liu.blog.service.impl;


import com.liu.blog.dao.UserDao;
import com.liu.blog.entity.User;
import com.liu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User服务层
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return null;
    }
}
