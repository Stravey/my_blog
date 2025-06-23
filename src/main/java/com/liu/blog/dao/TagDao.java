package com.liu.blog.dao;

import com.liu.blog.entity.Blog;
import com.liu.blog.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Tag表接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-23
 */
@Repository
public interface TagDao {

    /**
     * 添加标签
     * @param tag 标签对象
     */
    void addOneTag(Tag tag);

    /**
     * 获取标签
     * @param id id
     * @return 标签
     */
    Tag findOneTag(Long id);

    /**
     * 根据tagName获取标签
     * @param tagName 标签名称
     * @return 标签
     */
    Tag findOneTagByTagName(String tagName);

    /**
     * 分页查询标签
     * @param start 开始
     * @param count 数量
     * @return 标签集合
     */
    List<Tag> listTag(int start, int count);

    /**
     * 获取所有标签
     * @return 标签集合
     */
    List<Tag> findAllTag();

    /**
     * 获取所有发布的标签
     * @return 标签集合
     */
    List<Tag> findAllTagPublish();

    /**
     * 更新标签
     * @param tag 标签
     */
    void updateOneTag(Tag tag);

    /**
     * 删除标签
     * @param id 标签id
     */
    void deleteOneTag(Long id);

    /**
     * 遍历查找标签
     * @param map map对象
     * @return 标签集合
     */
    List<Tag> findTagForeach(Map<String, Object> map);

    /**
     * 通过tagId获取Blog的Id
     * @param tagId 标签id
     * @return  blogId集合
     */
    List<Long> findBlogIdByTagId(Long tagId);

    /**
     * 标签排序
     * @return 标签集合
     */
    List<Tag> OrderFindAllTag();

    /**
     * 通过标签获取博客
     * @param tagId 标签id
     * @return 博客集合
     */
    List<Blog> findBlogByTagId(Long tagId);

    /**
     * 获取六个标签
     * @return 标签集合
     */
    List<Tag> findSixTag();

}
