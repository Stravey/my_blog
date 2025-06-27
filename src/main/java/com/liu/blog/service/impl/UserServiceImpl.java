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
@SuppressWarnings("all")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 通过账号和密码查找用户
     * @param username 用户
     * @param password 密码
     * @return 查找到的用户
     */
    @Override
    public User checkUser(String username, String password) {
        User user = userDao.findOneUserByUsernameAndPassword(username, password);
        return user;
    }

    /**
     * 通过账号查找用户
     * @param username 用户
     * @return 查找到的用户
     */
    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findOneUserByUsername(username);
        return user;
    }
}
