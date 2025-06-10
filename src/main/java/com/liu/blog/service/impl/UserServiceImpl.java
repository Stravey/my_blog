package com.liu.blog.service.impl;


import com.liu.blog.dao.UserRepository;
import com.liu.blog.pojo.User;
import com.liu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username,String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

}
