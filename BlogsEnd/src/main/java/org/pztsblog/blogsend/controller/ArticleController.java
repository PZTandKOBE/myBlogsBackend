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

    @PostMapping
    public Result<String> publish(@RequestBody BlogArticle article) {
        if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
            return Result.error(400, "文章标题不能为空");
        }
        if (article.getContent() == null || article.getContent().trim().isEmpty()) {
            return Result.error(400, "文章内容不能为空");
        }

        article.setUserId(UserContext.getUserId());
        blogArticleService.publishArticle(article);

        return Result.success(article.getStatus() == 1 ? "发布成功！" : "草稿保存成功！");
    }

    /**
     * 🌟 修改点：增加了 keyword 参数用于模糊搜索
     */
    @GetMapping("/list")
    public Result<PageInfo<BlogArticle>> getList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        PageInfo<BlogArticle> pageInfo = blogArticleService.getArticleList(categoryId, keyword, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/{id}")
    public Result<BlogArticle> getDetail(@PathVariable Long id) {
        BlogArticle article = blogArticleService.getArticleDetail(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
        return Result.success(article);
    }

    @PutMapping
    public Result<String> update(@RequestBody BlogArticle article) {
        if (article.getId() == null) {
            return Result.error(400, "文章ID不能为空");
        }
        blogArticleService.updateArticle(article);
        return Result.success("修改成功！");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        blogArticleService.deleteArticle(id);
        return Result.success("删除成功！");
    }
}