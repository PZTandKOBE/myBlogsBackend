package org.pztsblog.blogsend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.pztsblog.blogsend.entity.BlogArticle;
import org.pztsblog.blogsend.mapper.BlogArticleMapper;
import org.pztsblog.blogsend.service.BlogArticleService;
import org.pztsblog.blogsend.service.RagService; // 🌟 注入 RAG 服务接口
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogArticleServiceImpl implements BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private RagService ragService; // 🌟 注入异步推送服务

    @Override
    public void publishArticle(BlogArticle article) {
        // 1. 设置默认状态与初始阅读量
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        article.setViewCount(0);

        // 2. 写入 MySQL 数据库
        blogArticleMapper.insertArticle(article);

        // 3. RAG 逻辑：只有“已发布”的文章才进入向量库进行切片
        if (article.getStatus() == 1) {
            // 注意：insertArticle 后 MyBatis 会将自增 ID 回填到 article 对象中
            ragService.pushArticleToVectorDb(article.getId(), article.getTitle(), article.getContent());
        }
    }

    @Override
    public PageInfo<BlogArticle> getArticleList(Integer categoryId, String keyword, int pageNum, int pageSize) {
        // 保留你最新的 keyword 搜索逻辑
        PageHelper.startPage(pageNum, pageSize);
        List<BlogArticle> list = blogArticleMapper.selectArticleList(categoryId, keyword);
        return new PageInfo<>(list);
    }

    @Override
    public BlogArticle getArticleDetail(Long id) {
        // 1. 阅读量自增
        blogArticleMapper.incrementViewCount(id);
        // 2. 查询详情
        return blogArticleMapper.selectArticleById(id);
    }

    @Override
    public void updateArticle(BlogArticle article) {
        // 1. 首先执行数据库更新操作
        blogArticleMapper.updateArticle(article);

        // 2. 关键点：为了保证推送给 AI 的内容是完整且最新的，根据 ID 重新从数据库捞取一次
        // 这样可以避免前端在 PUT 请求中只传了部分修改字段导致 AI 知识库缺失数据
        BlogArticle latestArticle = blogArticleMapper.selectArticleById(article.getId());

        // 3. RAG 逻辑：如果文章处于发布状态，同步更新向量库
        if (latestArticle != null && latestArticle.getStatus() == 1) {
            ragService.pushArticleToVectorDb(latestArticle.getId(), latestArticle.getTitle(), latestArticle.getContent());
        }
    }

    @Override
    public void deleteArticle(Long id) {
        // 执行物理删除
        blogArticleMapper.deleteArticleById(id);

        // TODO: 如果后期 Python 端增加了 /delete_embed 接口，可在此处追加异步删除向量的操作
    }
}