package org.pztsblog.blogsend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pztsblog.blogsend.entity.SysUser;

@Mapper // 这个注解告诉 Spring Boot 这是一个 MyBatis 的 Mapper
public interface SysUserMapper {
    // 定义一个方法：根据 ID 查询用户
    SysUser selectUserById(Long id);
    SysUser selectByUsername(String username);
    int insertUser(SysUser user);
}