package com.liu.blog.service.impl;


import com.github.pagehelper.PageInfo;
import com.liu.blog.dao.TagDao;
import com.liu.blog.entity.Tag;
import com.liu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tag服务层
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public boolean addTag(Tag tag) {
        return false;
    }

    @Override
    public Tag findTag(Long id) {
        return null;
    }

    @Override
    public PageInfo<Tag> listTag(int start, int limit) {
        return null;
    }

    @Override
    public boolean updateTag(Tag tag) {
        return false;
    }

    @Override
    public void deleteTag(Long id) {

    }

    @Override
    public List<Tag> findSomeTag(String ids) {
        return List.of();
    }

    @Override
    public PageInfo<Tag> findAllTagsTop(int limit) {
        return null;
    }

    @Override
    public List<Tag> orderFindAll(int limit) {
        return List.of();
    }

    @Override
    public List<Tag> findSixTags() {
        return List.of();
    }
}
