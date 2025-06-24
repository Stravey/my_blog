package com.liu.blog.service.impl;


import com.github.pagehelper.PageInfo;
import com.liu.blog.dao.BlogDao;
import com.liu.blog.entity.Blog;
import com.liu.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Blog服务层
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Override
    public boolean addBlog(Blog blog) {
        return false;
    }

    @Override
    public Blog findBlog(Long id) {
        return null;
    }

    @Override
    public Blog findBlogAndUpdateViews(Long id) {
        return null;
    }

    @Override
    public PageInfo<Blog> listBlog(int start, int count, String title, Long TypeId, Boolean recommend) {
        return null;
    }

    @Override
    public boolean updateBlog(Blog blog) {
        return false;
    }

    @Override
    public void deleteBlog(Long id) {

    }

    @Override
    public PageInfo<Blog> listBlogs(int start, int count) {
        return null;
    }

    @Override
    public PageInfo<Blog> listBlogs(int count) {
        return null;
    }

    @Override
    public Integer getTotalBlogsCount() {
        return 0;
    }

    @Override
    public Map listQueryBlogs(String query, int start, int count) {
        return Map.of();
    }

    @Override
    public PageInfo<Blog> listBlogsByTypeId(Long TypeId, int start, int count) {
        return null;
    }

    @Override
    public Map<String, List<Blog>> archiverBlog() {
        return Map.of();
    }

    @Override
    public List<Blog> findThreeBlogs() {
        return List.of();
    }
}
