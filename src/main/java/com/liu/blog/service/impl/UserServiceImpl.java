package com.liu.blog.service.impl;


import com.liu.blog.dao.UserRepository;
import com.liu.blog.pojo.User;
import com.liu.blog.service.UserService;
import com.liu.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username,String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return user;
    }

}
