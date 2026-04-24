package org.pztsblog.blogsend.service.impl;

import org.pztsblog.blogsend.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RagServiceImpl implements RagService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rag.service.url:http://127.0.0.1:8000/api/v1/embed}")
    private String ragUrl;

    @Async("taskExecutor") // 指定使用异步执行，即使不指定默认也会使用简单线程池
    @Override
    public void pushArticleToVectorDb(Long id, String title, String content) {
        try {
            // 构造符合 Python 端接口定义的 Request Body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("id", id);
            requestBody.put("title", title);
            requestBody.put("content", content);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // 发起 POST 请求
            Map<String, Object> response = restTemplate.postForObject(ragUrl, request, Map.class);

            if (response != null && "success".equals(response.get("status"))) {
                System.out.println("[RAG服务] 文章ID: " + id + " 向量化推送成功");
            } else {
                System.err.println("[RAG服务] 文章ID: " + id + " 推送返回异常状态: " + response);
            }

        } catch (Exception e) {
            // 异常捕获，确保 AI 服务的波动不会导致主业务受损
            System.err.println("[RAG服务] 无法连接到 Python 微服务或推送失败，错误详情: " + e.getMessage());
        }
    }
}