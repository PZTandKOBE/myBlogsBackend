package org.pztsblog.blogsend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private String signature;
    private Date createTime;
    private Date updateTime;
}