package com.liu.blog.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liu.blog.dao.TagDao;
import com.liu.blog.entity.Blog;
import com.liu.blog.entity.Tag;
import com.liu.blog.service.TagService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;


/**
 * Tag服务层
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@SuppressWarnings("all")
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addTag(Tag tag) {
        Tag t = tagDao.findOneTagByTagName(tag.getName());
        if(t != null) {
            return false;
        }
        tagDao.addOneTag(tag);
        return true;
    }

    @Override
    public Tag findTag(Long id) {
        return tagDao.findOneTag(id);
    }

    @Override
    public PageInfo<Tag> listTag(int start, int limit) {
        PageHelper.startPage(start, limit);
        List<Tag> tagList = tagDao.findAllTag();
        PageInfo<Tag> pageInfo = new PageInfo<Tag>(tagList);
        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateTag(Tag tag) {
        Tag t = tagDao.findOneTagByTagName(tag.getName());
        if(t != null) {
            return false;
        }
        tagDao.updateOneTag(tag);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTag(Long id) {
        tagDao.deleteOneTag(id);
    }

    @Override
    public List<Tag> findAllTag() {
        List<Tag> tagList = tagDao.findAllTag();
        return tagList;
    }

    @Override
    public List<Tag> findSomeTag(String ids) {
        Map<String,Object> map = convertMapByIds(ids);
        List<Long> ids_ = (List<Long>) map.get("ids");
        if(ids_ != null && ids_.size() > 0) {
            List<Tag> tagList = tagDao.findTagForeach(map);
            return tagList;
        } else {
            return null;
        }
    }

    @Override
    public PageInfo<Tag> findAllTagsTop(int limit) {
        PageHelper.startPage(0,limit);
        List<Tag> tagList = tagDao.findAllTag();
        PageInfo<Tag> pageInfo = new PageInfo<Tag>(tagList);
        for(int i = 0;i < tagList.size();i++) {
            Long id = pageInfo.getList().get(i).getId();
            List<Blog> blogs = tagDao.findBlogByTagId(id);
            pageInfo.getList().get(i).setBlogList(blogs);
        }
        return pageInfo;
    }

    @Override
    public List<Tag> orderFindAll(int limit) {
        List<Tag> tagList = tagDao.findAllTagPublish();
        Collections.sort(tagList, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.getBlogList().size() - o1.getBlogList().size();
            }
        });
        return tagList;
    }

    @Override
    public List<Tag> findSixTags() {
        return tagDao.findSixTag();
    }

    public Map<String,Object> convertMapByIds(String ids_) {
        HashMap<String,Object> map = new HashMap<>();
        List<Long> ids = new ArrayList<Long>();
        if(!StringUtils.isEmpty(ids_)) {
            String[] split = ids_.split(",");
            for(int i = 0;i < split.length;i++) {
                Long id = Long.parseLong(split[i]);
                ids.add(id);
            }
        }
        map.put("ids",ids);
        return map;
    }
}
