package com.liu.blog.dao;


import com.liu.blog.entity.Blog;
import com.liu.blog.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Blog表接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-23
 */
@Repository
public interface BlogDao {

    /**
     * 保存Blog并返回自增主键
     * @param blog 博客对象
     */
    void addOneBlog(Blog blog);

    /**
     * 把Blog的Tag集合记录加入tag_blog表中
     * @param blog 博客对象
     */
    void addBlog_Tags(Blog blog);

    /**
     * 不建议使用
     * 把Blog的Tag集合记录更新tag_blog表中
     * 如要更新 先删除原有的数据 把现有的数据添加上去
     * @param blog 博客对象
     */
    @Deprecated
    void updateBlog_Tags(Blog blog);

    /**
     * 通过标签获取博客
     * @param id 博客的主键Id
     * @return 博客对象
     */
    Blog findOneBlogHaveTags(Long id);

    /**
     * 不通过标签获取博客
     * @param id 博客的主键Id
     * @return 博客对象
     */
    Blog findOneBlogNoTags(Long id);

    /**
     * 通过博客的id找到标签集合
     * @param id 博客的主键Id
     * @return 标签集合
     */
    List<Tag> findTagsByBlogId(Long id);

    /**
     * 条件查询博客
     * @param title 博客标题
     * @param typeId 博客类型
     * @param recommend 博客是否推荐
     * @return 博客集合
     */
    List<Blog> findAllBlogs(String title,Long typeId,Boolean recommend);

    /**
     * 更新博客
     * @param blog 博客对象
     */
    void updateOneBlog(Blog blog);

    /**
     * 删除博客
     * @param id 博客的主键id
     */
    void deleteOneBlog(Long id);

    /**
     * 删除博客标签
     * @param id 博客的主键id
     */
    void deleteTagsByBlogId(Long id);

    /**
     * 获取所有博客
     * @return 博客集合
     */
    List<Blog> findAllBlogs();

    /**
     * 获取最新推荐的博客
     * @return 博客集合
     */
    List<Blog> findBlogsTop();

    /**
     * 获取博客数量
     * @return 博客数量
     */
    Integer getBlogsCount();

    /**
     * 通过title或者description查询Blog
     * @param query title 或 description
     * @return 博客集合
     */
    List<Blog> findQueryBlogs(String query);

    /**
     * 更新评论
     * @param blog 博客对象
     */
    void updateViews(Blog blog);

    /**
     * 通过类型id获取博客
     * @param typeId 博客类型
     * @return 博客集合
     */
    List<Blog> findBlogsByTypeId(Long typeId);

    /**
     * 获取归档所有年份的博客
     * @return 博客集合
     */
    List<String> findGroupYear();

    /**
     * 根据年份获取博客
     * @param year 年份
     * @return 博客集合
     */
    List<Blog> findBlogsByYear(String year);

    /**
     * 获取3个博客
     * @return 博客集合
     */
    List<Blog> findThreeBlogs();

}
