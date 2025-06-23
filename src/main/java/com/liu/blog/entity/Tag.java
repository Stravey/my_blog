package com.liu.blog.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tag实体类
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-22
 */
@Setter
@Getter
public class Tag implements Serializable {

    private Long id;

    private String name;

    private List<Blog> blogList = new ArrayList<>();

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blogList=" + blogList +
                '}';
    }
}
