package org.pztsblog.blogsend.vo;

import lombok.Data;
import org.pztsblog.blogsend.entity.BlogComment;

@Data
public class CommentVO extends BlogComment {
    // 额外追加的用户字段，专门用来给前端展示
    private String nickname;
    private String username;
    // 如果你的 sys_user 表里有头像字段，也可以加在这里，比如 private String avatar;
}