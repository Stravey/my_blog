package com.liu.blog.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liu.blog.dao.BlogDao;
import com.liu.blog.entity.Blog;
import com.liu.blog.entity.Tag;
import com.liu.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Blog服务层
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@SuppressWarnings("all")
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    /**
     *
     * @param blog
     * @return
     */
    @Override
    public boolean addBlog(Blog blog) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            blog.setViews(0);
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blogDao.addOneBlog(blog);

            if(blog.getTags() != null && !blog.getTags().isEmpty()) {
                blogDao.addBlog_Tags(blog);
            }
            dataSourceTransactionManager.commit(transactionStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            dataSourceTransactionManager.rollback(transactionStatus);
            return false;
        }
    }

    /**
     * 获取Blog
     * @param id 博客主键id
     * @return 博客
     */
    @Override
    public Blog findBlog(Long id) {
        List<Tag> tags = blogDao.findTagsByBlogId(id);
        Blog blog = null;
        if(tags != null && !tags.isEmpty()) {
            blog = blogDao.findOneBlogHaveTags(id);
        } else {
            blog = blogDao.findOneBlogNoTags(id);
        }
        blog.initTagIds();
        return blog;
    }

    /**
     * 获得博客并更新浏览次数
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog findBlogAndUpdateViews(Long id) {
        List<Tag> tags = blogDao.findTagsByBlogId(id);
        Blog blog = null;
        // 根据博客是否有标签
        if(tags != null && !tags.isEmpty()) {
            blog = blogDao.findOneBlogNoTags(id);
        } else {
            blog = blogDao.findOneBlogNoTags(id);
        }
        Integer views = blog.getViews();
        blog.setViews(views++);
        blogDao.updateViews(blog);
        blog.initTagIds();
        return blog;
    }

    /**
     *
     * @param start
     * @param count
     * @param title
     * @param TypeId
     * @param recommend
     * @return
     */
    @Override
    public PageInfo<Blog> listBlog(int start, int count, String title, Long TypeId, Boolean recommend) {
        PageHelper.startPage(start, count);
        List<Blog> blogList = blogDao.findAllBlogs(title, TypeId, recommend);
        return new PageInfo<>(blogList);
    }

    /**
     * 更新博客
     * @param blog 博客
     * @return 是否更新
     */
    @Override
    public boolean updateBlog(Blog blog) {
        // 开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

        try {
            blog.setUpdateTime(new Date());
            blogDao.updateOneBlog(blog);
            List<Tag> tags = blogDao.findTagsByBlogId(blog.getId());
            if(blog.getTags() != null && !blog.getTags().isEmpty()) {
                //判断之前的tag_blog表中是否有数据
                if(tags != null && !tags.isEmpty()) {
                    blogDao.deleteTagsByBlogId(blog.getId());
                    blogDao.addBlog_Tags(blog);
                } else {
                    blogDao.addBlog_Tags(blog);
                }
            } else {
                if(tags != null && !tags.isEmpty()) {
                    blogDao.deleteTagsByBlogId(blog.getId());
                }
            }
            dataSourceTransactionManager.commit(transactionStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            dataSourceTransactionManager.rollback(transactionStatus);
            return false;
        }
    }

    /**
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlog(Long id) {
        List<Tag> tags = blogDao.findTagsByBlogId(id);
        if(tags != null && !tags.isEmpty()) {
            blogDao.deleteTagsByBlogId(id);
        }
        blogDao.deleteOneBlog(id);
    }

    /**
     * 分页查询所有博客
     * @param start 开始
     * @param count 数量
     * @return 博客集合
     */
    @Override
    public PageInfo<Blog> listBlogs(int start, int count) {
        PageHelper.startPage(start, count);
        List<Blog> blogList = blogDao.findAllBlogs();
        return new PageInfo<>(blogList);
    }

    /**
     * 查询最新推荐博客
     * @param count 数量
     * @return 博客集合
     */
    @Override
    public PageInfo<Blog> listBlogs(int count) {
        PageHelper.startPage(count);
        List<Blog> blogList = blogDao.findBlogsTop();
        return new PageInfo<>(blogList);
    }

    /**
     * 查询博客总数
     * @return 博客总数
     */
    @Override
    public Integer getTotalBlogsCount() {
        return blogDao.getBlogsCount();
    }

    /**
     * 条件查询博客
     * @param query
     * @param start
     * @param count
     * @return
     */
    @Override
    public Map listQueryBlogs(String query, int start, int count) {
        PageHelper.startPage(start, count);
        List<Blog> blogList = blogDao.findQueryBlogs(query);
        int blogCount = blogList.size();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        HashMap<String,Object> map = new HashMap<>();
        map.put("blogCount",blogCount);
        map.put("query",query);
        return map;
    }

    /**
     * 通过type的id分页查询有关所有的Blog
     * @param TypeId
     * @param start
     * @param count
     * @return
     */
    @Override
    public PageInfo<Blog> listBlogsByTypeId(Long TypeId, int start, int count) {
        PageHelper.startPage(start,count);
        List<Blog> blogs = blogDao.findBlogsByTypeId(TypeId);
        return new PageInfo<>(blogs);
    }

    /**
     * 通过tag的id分页查询有关所有的Blog
     * @param TagId
     * @param start
     * @param count
     * @return
     */
    @Override
    public PageInfo<Blog> listBlogsByTagId(Long TagId, int start, int count) {
        PageHelper.startPage(start,count);
        List<Blog> blogList = blogDao.findBlogsByTypeId(TagId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        for(int i = 0;i < pageInfo.getList().size();i++) {
            Long id = pageInfo.getList().get(i).getId();
            List<Tag> tags = blogDao.findTagsByBlogId(id);
            pageInfo.getList().get(i).setTags(tags);
        }
        return pageInfo;
    }

    /**
     * 归档页面
     * @return
     */
    @Override
    public Map<String, List<Blog>> archiverBlog() {
        List<String> years = blogDao.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for(String year : years) {
            List<Blog> blogs = blogDao.findBlogsByYear(year);
            map.put(year,blogs);
        }
        return map;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Blog> findThreeBlogs() {
        return blogDao.findThreeBlogs();
    }
}
