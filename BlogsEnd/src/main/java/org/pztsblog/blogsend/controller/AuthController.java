package org.pztsblog.blogsend.controller;

import org.pztsblog.blogsend.common.Result;
import org.pztsblog.blogsend.entity.SysUser;
import org.pztsblog.blogsend.mapper.SysUserMapper;
import org.pztsblog.blogsend.service.SysUserService;
import org.pztsblog.blogsend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || password == null) {
            return Result.error(400, "账号或密码不能为空");
        }

        SysUser user = sysUserMapper.selectByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            return Result.error(400, "账号或密码错误");
        }

        String token = JwtUtils.generateToken(user.getId(), user.getUsername());
        return Result.success(token);
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody SysUser user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return Result.error(400, "账号不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return Result.error(400, "密码不能少于6位");
        }

        // 调用 Service 执行注册逻辑
        String errorMsg = sysUserService.register(user);

        if (errorMsg != null) {
            return Result.error(400, errorMsg);
        }

        return Result.success("注册成功！");
    }
}