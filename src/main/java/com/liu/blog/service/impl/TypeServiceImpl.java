package com.liu.blog.service.impl;


import com.liu.blog.dao.TypeRepository;
import com.liu.blog.exception.NotFoundException;
import com.liu.blog.pojo.Type;
import com.liu.blog.service.TypeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("不存在该类型"));
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Transactional
    @Override
    public Type updateType(Long id,Type type) {
        Type t = typeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("不存在Id"));
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("不存在Id"));
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findTypeByName(name);
    }
}
