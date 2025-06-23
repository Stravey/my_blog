package com.liu.blog.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Comment实体类
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-23
 */
@Getter
@Setter
public class Comment implements Serializable {

    /**
     * comment表Id
     */
    private Long id;

    /**
     * 评论者的昵称
     */
    private String nickname;

    /**
     * 评论者的邮箱
     */
    private String email;

    /**
     * 评论者的内容
     */
    private String content;

    /**
     * 评论者的头像
     */
    private String avatar;

    /**
     * 评论创建时间
     */
    private Date createTime;

    /**
     * 一个评论对应一个博客
     */
    private Blog blog;

    /**
     * 一个评论对应一个博客
     */
    private List<Comment> comments = new ArrayList<>();

    /**
     * 子评论拥有父评论
     */
    private Comment parentComment;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", blog=" + blog +
                ", comments=" + comments +
                ", parentComment=" + parentComment +
                '}';
    }
}
