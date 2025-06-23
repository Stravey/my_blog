package com.liu.blog.dao;

import com.liu.blog.entity.Blog;
import com.liu.blog.entity.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Type表接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-23
 */
@Repository
public interface TypeDao {

    /**
     * 添加类型
     * @param type 类型
     */
    void addOneType(Type type);

    /**
     * 获取类型
     * @param id 类型id
     * @return 类型
     */
    Type findOneType(Long id);

    /**
     * 通过类型名称查找类型
     * @param typeName 类型名称
     * @return 类型
     */
    Type findOneTypeByTypeName(String typeName);

    /**
     * 分页查询类型
     * @param start 开始
     * @param count 数量
     * @return 类型集合
     */
    List<Type> listType(int start, int count);

    /**
     * 遍历类型
     * @return 类型集合
     */
    List<Type> findAllTypeForBlog();

    /**
     * 获取所有类型
     * @return 类型集合
     */
    List<Type> findAllType();

    /**
     * 更新类型
     * @param type 类型
     */
    void updateOneType(Type type);

    /**
     * 删除类型
     * @param id 类型id
     */
    void deleteOneType(Long id);

    /**
     * 类型排序
     * @return 类型集合
     */
    List<Type> OrderFindAllType();

    /**
     * 通过类型id查找博客
     * @param id 类型id
     * @return 博客集合
     */
    List<Blog> findBlogsByTypeId(Long id);

    /**
     * 获取六个类型
     * @return 类型集合
     */
    List<Type> findSixType();
}
