package com.liu.blog.service.impl;


import com.liu.blog.dao.BlogRepository;
import com.liu.blog.exception.NotFoundException;
import com.liu.blog.pojo.Blog;
import com.liu.blog.service.BlogService;
import com.liu.blog.vo.BlogQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("博客不存在，id: " + id));
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    list.add(cb.like(root.get("title"),"%" + blog.getTitle() + "%"));
                }
                if(blog.getTypeId() != null) {
                    list.add(cb.equal(root.get("type").get("id"), blog.getTypeId()));
                }
                if(blog.isRecommend()) {
                    list.add(cb.equal(root.get("recommend"), blog.isRecommend()));
                }
                query.where(list.toArray(new Predicate[list.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        if(blog == null) {
            throw new NotFoundException("更新的博客不能为空！");
        }
        Blog oldBlog = blogRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("该博客不存在!"));
        BeanUtils.copyProperties(blog, oldBlog);
        return blogRepository.save(oldBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
