package org.pztsblog.blogsend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 开启异步任务支持
@SpringBootApplication
public class BlogsEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogsEndApplication.class, args);
    }

}