package org.pztsblog.blogsend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BlogArticle {
    private Long id;
    private Long userId; // 作者ID
    private String title; // 文章标题
    private String content; // 文章正文 (Markdown或HTML)
    private String coverImage; // 封面图URL
    private Integer status; // 状态: 0-草稿, 1-已发布
    private Integer categoryId;
    private Integer viewCount; // 阅读量
    private Date createTime;
    private Date updateTime;
}