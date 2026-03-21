package org.pztsblog.blogsend.entity;

import lombok.Data;

@Data
public class BlogMusic {
    private Long id;
    private String name;      // 歌曲名称
    private String artist;    // 歌手
    private String url;       // 歌曲链接
    private String coverUrl;  // 封面链接
    private Integer isActive; // 是否启用：1启用，0停用
}
