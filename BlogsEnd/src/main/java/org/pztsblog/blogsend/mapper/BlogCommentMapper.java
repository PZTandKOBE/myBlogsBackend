package org.pztsblog.blogsend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.pztsblog.blogsend.entity.BlogComment;
import org.pztsblog.blogsend.vo.CommentVO;

import java.util.List;

@Mapper
public interface BlogCommentMapper {
    // 发表评论
    int insertComment(BlogComment comment);

    // 根据文章ID查询所有评论，按时间倒序（最新的在最上面）
    List<BlogComment> selectByArticleId(Long articleId);
    // ⭐️ 新增：联表查询评论和用户信息
    List<CommentVO> selectCommentsWithUser(Long articleId);
    int deleteComment(@Param("id") Long id, @Param("userId") Long userId);
}