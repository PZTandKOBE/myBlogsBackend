package org.pztsblog.blogsend.service.impl;

import org.pztsblog.blogsend.entity.BlogMusic;
import org.pztsblog.blogsend.mapper.BlogMusicMapper;
import org.pztsblog.blogsend.service.BlogMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BlogMusicServiceImpl implements BlogMusicService {

    @Autowired
    private BlogMusicMapper blogMusicMapper;

    @Override
    public List<BlogMusic> getMusicList() {
        return blogMusicMapper.selectMusicList();
    }
}