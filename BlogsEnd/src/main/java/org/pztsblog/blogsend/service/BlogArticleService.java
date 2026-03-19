package org.pztsblog.blogsend.service;

import com.github.pagehelper.PageInfo;
import org.pztsblog.blogsend.entity.BlogArticle;

public interface BlogArticleService {
    // 发布或保存文章
    void publishArticle(BlogArticle article);

    PageInfo<BlogArticle> getArticleList(Integer categoryId, int pageNum, int pageSize);

    BlogArticle getArticleDetail(Long id);

    // 更新文章
    void updateArticle(BlogArticle article);

    // 删除文章
    void deleteArticle(Long id);
}