package org.pztsblog.blogsend.controller;

import org.pztsblog.blogsend.common.Result;
import org.pztsblog.blogsend.entity.BlogMusic;
import org.pztsblog.blogsend.service.BlogMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private BlogMusicService blogMusicService;

    /**
     * 获取音乐列表（前端直接调用即可，无需传参）
     */
    @GetMapping("/list")
    public Result<List<BlogMusic>> getList() {
        return Result.success(blogMusicService.getMusicList());
    }
}