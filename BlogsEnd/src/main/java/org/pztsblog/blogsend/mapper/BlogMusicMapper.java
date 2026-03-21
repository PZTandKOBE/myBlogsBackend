package org.pztsblog.blogsend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pztsblog.blogsend.entity.BlogMusic;
import java.util.List;

@Mapper
public interface BlogMusicMapper {
    // 只获取已启用的音乐列表
    List<BlogMusic> selectMusicList();
}