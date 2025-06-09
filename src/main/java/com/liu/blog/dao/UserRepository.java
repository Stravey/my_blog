package com.liu.blog.dao;


import com.liu.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username,String password);

}
