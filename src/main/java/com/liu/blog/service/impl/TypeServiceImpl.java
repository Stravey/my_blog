package com.liu.blog.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liu.blog.dao.TypeDao;
import com.liu.blog.entity.Blog;
import com.liu.blog.entity.Type;
import com.liu.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Type服务层
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-27
 */
@SuppressWarnings("all")
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;

    /**
     * 添加type
     * @param type type对象
     * @return 布尔类型
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addType(Type type) {
        Type t = typeDao.findOneTypeByTypeName((type.getTypeName()));
        if(t != null) {
            return false;
        }
        typeDao.addOneType(type);
        return true;
    }

    /**
     * 返回查找对应的博客类型
     * @param id 类型id
     * @return 对应的类型
     */
    @Override
    public Type findType(Long id) {
        return typeDao.findOneType(id);
    }

    /**
     * 分页查询
     * @param start
     * @param count
     * @return
     */
    @Override
    public PageInfo<Type> listType(int start, int count) {
        PageHelper.startPage(start, count);
        List<Type> typeList = typeDao.findAllType();
        return new PageInfo<>(typeList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateType(Type type) {
        Type t = typeDao.findOneTypeByTypeName((type.getTypeName()));
        if(t != null) {
            return false;
        }
        typeDao.updateOneType(type);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteType(Long id) {
        typeDao.deleteOneType(id);
    }

    @Override
    public List<Type> findAllType() {
        List<Type> typeList = typeDao.findAllType();
        return typeList;
    }

    /**
     * 分页查询最新的type
     * @param count
     * @return
     */
    @Override
    public PageInfo<Type> findAllTypesTop(int count) {
        PageHelper.startPage(count);
        List<Type> typeList = typeDao.OrderFindAllType();
        PageInfo<Type> pageInfo = new PageInfo<>(typeList);
        for(int i = 0;i < typeList.size();i++) {
            Long id = pageInfo.getList().get(i).getId();
            List<Blog> blogs = typeDao.findBlogsByTypeId(id);
            pageInfo.getList().get(i).setBlogs(blogs);
        }
        return pageInfo;
    }

    @Override
    public List<Type> orderFindAllType() {
        List<Type> typeList = typeDao.findAllTypeForBlog();
        Collections.sort(typeList, new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                return o2.getBlogs().size() - o1.getBlogs().size();
            }
        });
        return typeList;
    }

    @Override
    public List<Type> findSixType() {
        List<Type> typeList = typeDao.findSixType();
        return typeList;
    }
}
