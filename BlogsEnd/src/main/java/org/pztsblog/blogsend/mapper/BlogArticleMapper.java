package org.pztsblog.blogsend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pztsblog.blogsend.entity.BlogArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogArticleMapper {
    // 插入一篇文章
    int insertArticle(BlogArticle article);
    List<BlogArticle> selectArticleList();

    // 根据 ID 查询文章
    BlogArticle selectArticleById(Long id);

    // 增加文章阅读量
    int incrementViewCount(Long id);

    // 更新文章
    int updateArticle(BlogArticle article);

    // 根据 ID 删除文章
    int deleteArticleById(Long id);

    List<BlogArticle> selectArticleList(@Param("categoryId") Integer categoryId);
}