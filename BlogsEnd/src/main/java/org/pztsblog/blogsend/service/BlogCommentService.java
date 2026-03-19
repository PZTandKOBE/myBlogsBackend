package org.pztsblog.blogsend.service;

import org.pztsblog.blogsend.entity.BlogComment;
import org.pztsblog.blogsend.vo.CommentVO;

import java.util.List;

public interface BlogCommentService {
    void addComment(BlogComment comment);
    List<CommentVO> getCommentsByArticleId(Long articleId);
    // 删除评论，返回错误信息（成功则返回 null）
    String deleteComment(Long id, Long userId);
}