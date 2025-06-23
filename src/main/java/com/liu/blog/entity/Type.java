package com.liu.blog.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Type实体类
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-23
 */
@Getter
@Setter
public class Type implements Serializable {

    /**
     * Type表Id
     */
    private Long id;

    /**
     * 类型名
     */
    private String typeName;

    /**
     * 单类型对应多博客
     */
    private List<Blog> blogs = new ArrayList<>();

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", blogs=" + blogs +
                '}';
    }
}
