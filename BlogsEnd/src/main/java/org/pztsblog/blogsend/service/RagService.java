package org.pztsblog.blogsend.service;

public interface RagService {
    /**
     * 将文章异步推送到向量数据库
     * @param id 文章MySQL主键
     * @param title 标题
     * @param content 正文
     */
    void pushArticleToVectorDb(Long id, String title, String content);
}