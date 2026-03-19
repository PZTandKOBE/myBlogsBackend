package org.pztsblog.blogsend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @RestController 表示这个类专门用于处理 HTTP 请求，并且所有方法都默认返回 JSON 格式的数据
 * @RequestMapping("/api") 给这个类里所有的接口加一个统一个前缀
 */
@RestController
@RequestMapping("/api")
public class TestController {

    /**
     * @GetMapping("/test") 表示处理 GET 类型的请求，完整路径是 /api/test
     */
    @GetMapping("/test")
    public Map<String, Object> test() {
        // 使用 Map 模拟返回一个 JSON 对象给前端
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "后端服务启动成功，数据库连接已准备就绪！");
        result.put("data", "Hello, Vue3!");
        return result;
    }
}