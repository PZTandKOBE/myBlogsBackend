package org.pztsblog.blogsend.controller;

import org.pztsblog.blogsend.common.Result;
import org.pztsblog.blogsend.entity.BlogComment;
import org.pztsblog.blogsend.service.BlogCommentService;
import org.pztsblog.blogsend.utils.UserContext;
import org.pztsblog.blogsend.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private BlogCommentService blogCommentService;

    /**
     * 发表评论
     */
    @PostMapping
    public Result<String> publishComment(@RequestBody BlogComment comment) {
        if (comment.getArticleId() == null) {
            return Result.error(400, "必须指定要评论的文章");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            return Result.error(400, "评论内容不能为空");
        }

        comment.setUserId(UserContext.getUserId());

        blogCommentService.addComment(comment);
        return Result.success("评论发表成功！");
    }


    /**
     * 获取某篇文章的评论列表 (带用户信息)
     */
    @GetMapping("/article/{articleId}")
    public Result<List<CommentVO>> getComments(@PathVariable Long articleId) {
        List<CommentVO> list = blogCommentService.getCommentsByArticleId(articleId);
        return Result.success(list);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        // 1. 从 JWT 拦截器存好的“储物柜”里，极其方便地拿出当前登录人的 ID
        Long currentUserId = UserContext.getUserId();

        // 2. 调用 Service 去删
        String errorMsg = blogCommentService.deleteComment(id, currentUserId);

        // 3. 判断结果
        if (errorMsg != null) {
            return Result.error(403, errorMsg); // 403 代表 Forbidden (无权限)
        }

        return Result.success("评论删除成功！");
    }
}