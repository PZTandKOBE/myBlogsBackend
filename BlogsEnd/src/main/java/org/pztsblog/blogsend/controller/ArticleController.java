package org.pztsblog.blogsend.controller;

import org.pztsblog.blogsend.common.Result;
import org.pztsblog.blogsend.entity.BlogArticle;
import org.pztsblog.blogsend.service.BlogArticleService;
import org.pztsblog.blogsend.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private BlogArticleService blogArticleService;

    /**
     * 发布/保存文章接口
     */
    @PostMapping
    public Result<String> publish(@RequestBody BlogArticle article) {
        // 简单校验标题和内容不能为空
        if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
            return Result.error(400, "文章标题不能为空");
        }
        if (article.getContent() == null || article.getContent().trim().isEmpty()) {
            return Result.error(400, "文章内容不能为空");
        }

        // 临时方案：这里为了测试方便，如果前端没传 userId，我们先写死为 1。
        // 等后续加上拦截器后，这里会直接从 JWT Token 里解析出真实的 userId。
        article.setUserId(UserContext.getUserId());

        blogArticleService.publishArticle(article);

        return Result.success(article.getStatus() == 1 ? "发布成功！" : "草稿保存成功！");
    }

    /**
     * 分页查询文章列表 (支持按分类筛选)
     */
    @GetMapping("/list")
    public Result<PageInfo<BlogArticle>> getList(
            // required = false 表示这个参数不是必须的，不传就是 null，查全部分类
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageInfo<BlogArticle> pageInfo = blogArticleService.getArticleList(categoryId, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 根据 ID 获取文章详情
     * @PathVariable 用于接收 URL 路径里的 {id} 变量
     */
    @GetMapping("/{id}")
    public Result<BlogArticle> getDetail(@PathVariable Long id) {
        BlogArticle article = blogArticleService.getArticleDetail(id);

        if (article == null) {
            return Result.error(404, "文章不存在");
        }

        return Result.success(article);
    }

    /**
     * 修改文章
     * @PutMapping 代表这是一个修改请求
     */
    @PutMapping
    public Result<String> update(@RequestBody BlogArticle article) {
        // 修改必须传文章的 ID 过来，不然数据库不知道你要改哪篇
        if (article.getId() == null) {
            return Result.error(400, "文章ID不能为空");
        }

        blogArticleService.updateArticle(article);
        return Result.success("修改成功！");
    }

    /**
     * 删除文章
     * @DeleteMapping 代表这是一个删除请求
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        blogArticleService.deleteArticle(id);
        return Result.success("删除成功！");
    }
}