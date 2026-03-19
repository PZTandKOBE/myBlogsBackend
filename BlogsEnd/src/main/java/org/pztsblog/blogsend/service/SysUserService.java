package org.pztsblog.blogsend.service;

import org.pztsblog.blogsend.entity.SysUser;

public interface SysUserService {
    // 定义获取用户信息的方法
    SysUser getUserById(Long id);
    String register(SysUser user);
}