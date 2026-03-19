package org.pztsblog.blogsend.controller;

import org.pztsblog.blogsend.common.Result;
import org.pztsblog.blogsend.entity.SysUser;
import org.pztsblog.blogsend.service.SysUserService;
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
}