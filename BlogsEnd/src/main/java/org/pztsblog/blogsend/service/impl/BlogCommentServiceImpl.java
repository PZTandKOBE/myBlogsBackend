package org.pztsblog.blogsend.service.impl;

import org.pztsblog.blogsend.entity.BlogComment;
import org.pztsblog.blogsend.mapper.BlogCommentMapper;
import org.pztsblog.blogsend.service.BlogCommentService;
import org.pztsblog.blogsend.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Override
    public void addComment(BlogComment comment) {
        blogCommentMapper.insertComment(comment);
    }

    @Override
    public List<CommentVO> getCommentsByArticleId(Long articleId) {
        return blogCommentMapper.selectCommentsWithUser(articleId);
    }

    @Override
    public String deleteComment(Long id, Long userId) {
        // 执行删除，并获取受影响的行数
        int rows = blogCommentMapper.deleteComment(id, userId);

        // 如果受影响的行数是 0，说明要么评论不存在，要么这条评论不是他发的
        if (rows == 0) {
            return "删除失败：评论不存在或您无权删除别人的评论";
        }

        return null; // 返回 null 代表删除成功
    }
}