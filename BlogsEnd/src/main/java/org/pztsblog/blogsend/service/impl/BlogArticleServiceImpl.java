package org.pztsblog.blogsend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.pztsblog.blogsend.entity.BlogArticle;
import org.pztsblog.blogsend.mapper.BlogArticleMapper;
import org.pztsblog.blogsend.service.BlogArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogArticleServiceImpl implements BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Override
    public void publishArticle(BlogArticle article) {
        // 如果前端没有传状态，默认当作发布 (1)
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        // 初始阅读量设为 0
        article.setViewCount(0);

        // 调用 Mapper 插入数据库
        blogArticleMapper.insertArticle(article);
    }

    @Override
    public PageInfo<BlogArticle> getArticleList(Integer categoryId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 把参数传给 Mapper
        List<BlogArticle> list = blogArticleMapper.selectArticleList(categoryId);
        return new PageInfo<>(list);
    }

    @Override
    public BlogArticle getArticleDetail(Long id) {
        // 1. 阅读量 +1
        blogArticleMapper.incrementViewCount(id);

        // 2. 查询出最新的文章数据并返回
        return blogArticleMapper.selectArticleById(id);
    }

    @Override
    public void updateArticle(BlogArticle article) {
        blogArticleMapper.updateArticle(article);
    }

    @Override
    public void deleteArticle(Long id) {
        blogArticleMapper.deleteArticleById(id);
    }
}