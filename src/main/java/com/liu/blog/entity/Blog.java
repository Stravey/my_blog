package com.liu.blog.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Blog实体类
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-22
 */
@Setter
@Getter
public class Blog implements Serializable {

    /**
     * Blog表Id
     */
    private Long id;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 博客图首地址
     */
    private String firstPicture;

    /**
     * 博客标记(原创 转载 翻译)
     */
    private String flag;

    /**
     * 博客浏览次数
     */
    private Integer views;

    /**
     * 博客赞赏
     */
    private Boolean appreciation;

    /**
     * 博客转载
     */
    private Boolean shareStatement;

    /**
     * 博客评论
     */
    private Boolean commentable;

    /**
     * 博客发布
     */
    private Boolean published;

    /**
     * 博客推荐
     */
    private Boolean recommend;

    /**
     * 博客创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 博客更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 博客类型 外键
     */
    private Type type;

    /**
     * 用户 外键
     */
    private User user;

    /**
     * 单个博客 多个标签
     */
    private List<Tag> tags = new ArrayList<>();

    /**
     * 单个博客 多个评论
     */
    private List<Comment> comments = new ArrayList<>();

    /**
     * 多Tag的Id
     */
    private String tagIds;

    /**
     * 博客描述
     */
    private String description;

    /**
     * 初始化TagsId
     */
    public void initTagIds() {
        this.tagIds = tagsToIds(this.getTags());
    }

    /**
     * 把tags集合的id拼接成tagIds 1,2,3
     * @param tags 博客的多个标签
     * @return 拼接成的Tags集合
     */
    private String tagsToIds(List<Tag> tags) {
        if(!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for(Tag tag : tags) {
                if(!flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", commentable=" + commentable +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", user=" + user +
                ", tags=" + tags +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
