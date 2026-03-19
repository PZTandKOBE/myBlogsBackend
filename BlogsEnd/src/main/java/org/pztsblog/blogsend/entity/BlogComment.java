package org.pztsblog.blogsend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BlogComment {
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private String imageUrl;
    private Date createTime;
}
