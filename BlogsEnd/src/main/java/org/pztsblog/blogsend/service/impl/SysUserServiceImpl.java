package org.pztsblog.blogsend.service.impl;

import org.pztsblog.blogsend.entity.SysUser;
import org.pztsblog.blogsend.mapper.SysUserMapper;
import org.pztsblog.blogsend.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // ⭐️ 这个注解非常重要，告诉 Spring 这是一个业务逻辑类，把它交由 Spring 管理
public class SysUserServiceImpl implements SysUserService {

    // 在 Service 里注入 Mapper（厨师让采购员去拿菜）
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser getUserById(Long id) {
        // 以后这里可以加很多逻辑，比如判断 id 是否合法、是否被拉黑等
        // 目前直接调用 Mapper 去查数据库
        return sysUserMapper.selectUserById(id);
    }

    @Override
    public String register(SysUser user) {
        // 1. 检查账号是否已经存在
        SysUser existUser = sysUserMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return "该账号已被注册";
        }

        // 2. 如果不存在，赋予一个默认昵称（可选），然后保存到数据库
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname("新用户_" + user.getUsername());
        }

        sysUserMapper.insertUser(user);

        return null; // 返回 null 代表没有任何错误，注册成功
    }
}