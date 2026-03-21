package org.pztsblog.blogsend.controller;

import org.pztsblog.blogsend.common.Result;
import org.pztsblog.blogsend.entity.SysUser;
import org.pztsblog.blogsend.service.SysUserService;
import org.pztsblog.blogsend.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        SysUser user = sysUserService.getUserById(id);
        if (user == null) {
            return Result.error(404, "该用户不存在");
        }
        return Result.success(user); // 用 Result 包装数据
    }

    /**
     * 获取当前登录用户的信息
     */
    @GetMapping("/current")
    public Result<SysUser> getCurrentUserInfo() {
        // 1. 从 JWT 拦截器的储物柜里，直接拿到当前请求用户的 ID
        Long userId = UserContext.getUserId();

        // 2. 去数据库查这个人的完整信息
        SysUser user = sysUserService.getUserById(userId);

        if (user == null) {
            return Result.error(404, "找不到该用户");
        }

        // 3. 极其重要的安全操作：返回给前端之前，把密码清空！绝对不能把密码泄露给前端！
        user.setPassword(null);

        return Result.success(user);
    }
}