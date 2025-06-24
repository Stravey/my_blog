package com.liu.blog.service;


import com.github.pagehelper.PageInfo;
import com.liu.blog.entity.Tag;

import java.util.List;

/**
 * Tag服务层接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
public interface TagService {

    boolean addTag(Tag tag);

    Tag findTag(Long id);

    PageInfo<Tag> listTag(int start, int limit);

    boolean updateTag(Tag tag);

    void deleteTag(Long id);

    List<Tag> findSomeTag(String ids);

    PageInfo<Tag> findAllTagsTop(int limit);

    List<Tag> orderFindAll(int limit);

    List<Tag> findSixTags();
}
