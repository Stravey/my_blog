package com.liu.blog.service;


import com.liu.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    Type updateType(Long id,Type type);

    void deleteType(Long id);

    Type getTypeByName(String name);
}
