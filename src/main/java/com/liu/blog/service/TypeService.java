package com.liu.blog.service;


import com.github.pagehelper.PageInfo;
import com.liu.blog.entity.Type;

import java.util.List;

/**
 * Type服务层接口
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
public interface TypeService {

    boolean addType(Type type);

    Type findType(Long id);

    PageInfo<Type> listType(int start, int count);

    boolean updateType(Type type);

    void deleteType(Long id);

    List<Type> findAllType();

    PageInfo<Type> findAllTypesTop(int count);

    List<Type> orderFindAllType();

    List<Type> findSixType();
}
