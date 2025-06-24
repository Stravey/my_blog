package com.liu.blog.service;

import com.github.pagehelper.PageInfo;
import com.liu.blog.entity.Blog;

import java.util.List;
import java.util.Map;

/**
 * Blog服务层接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
public interface BlogService {

    boolean addBlog(Blog blog);

    Blog findBlog(Long id);

    Blog findBlogAndUpdateViews(Long id);

    PageInfo<Blog> listBlog(int start, int count, String title, Long TypeId, Boolean recommend);

    boolean updateBlog(Blog blog);

    void deleteBlog(Long id);

    PageInfo<Blog> listBlogs(int start,int count);

    PageInfo<Blog> listBlogs(int count);

    Integer getTotalBlogsCount();

    Map listQueryBlogs(String query, int start, int count);

    PageInfo<Blog> listBlogsByTypeId(Long TypeId,int start,int count);

    Map<String, List<Blog>> archiverBlog();

    List<Blog> findThreeBlogs();
}
