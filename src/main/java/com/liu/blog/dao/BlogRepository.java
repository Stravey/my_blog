package com.liu.blog.dao;


import com.liu.blog.pojo.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogRepository extends JpaRepository<Blog, Long> , JpaSpecificationExecutor<Blog> {
}
