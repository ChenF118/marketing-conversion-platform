package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.Jwt;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Admin;
import com.fans.mapper.AdminMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 账号与权限 */
@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final Jwt jwt;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminService(AdminMapper adminMapper, Jwt jwt) {
        this.adminMapper = adminMapper;
        this.jwt = jwt;
    }

    @Transactional
    public Map<String, Object> register(Map<String, Object> form) {
        String username = Req.text(form.get("username"));
        Long exists = adminMapper.selectCount(new QueryWrapper<Admin>().eq("username", username));
        if (exists != null && exists > 0) {
            return Api.res(201, "该用户名已被占用！", List.of());
        }
        Admin admin = new Admin();
        admin.username = username;
        admin.password = encoder.encode(Req.text(form.get("password")));
        admin.header = Req.text(form.get("imageUrl"));
        admin.power = 0; // 公开注册一律为运营角色，超管由数据库/后台分配
        adminMapper.insert(admin);
        return Api.res(200, "成功", List.of());
    }

    public Map<String, Object> login(Map<String, Object> form) {
        String username = Req.text(form.get("username"));
        String rawPassword = Req.text(form.get("password"));
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", username).last("LIMIT 1"));

        if (admin == null) {
            return Api.res(201, "账号不存在！", List.of());
        }
        if (rawPassword != null && admin.password != null && encoder.matches(rawPassword, admin.password)) {
            String token = jwt.sign(String.valueOf(admin.id));
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("code", 200);
            body.put("msg", "获取成功！");
            body.put("Token", token);
            body.put("IDcode", admin.id);
            return body;
        }
        return Api.res(202, "密码错误！", List.of());
    }

    /** 替代原 /admin/user：返回用户资料，但不再返回 password */
    public Map<String, Object> profile(Integer id) {
        if (!CurrentUser.isAdmin() && !java.util.Objects.equals(id, CurrentUser.id())) {
            throw new com.fans.common.ForbiddenException("无权查看其他用户");
        }
        Admin admin = adminMapper.selectById(id);
        if (admin == null) return Api.res(201, "用户不存在！", List.of());
        Map<String, Object> data = Maps.pick(
                "id", admin.id,
                "username", admin.username,
                "header", admin.header,
                "power", admin.power);
        return Api.res(200, "获取成功！", data);
    }

    @Transactional
    public Map<String, Object> changePassword(Map<String, Object> form) {
        Integer id = Req.toInteger(form.get("id"));
        if (!CurrentUser.isAdmin() && !java.util.Objects.equals(id, CurrentUser.id())) {
            throw new com.fans.common.ForbiddenException("无权修改其他用户");
        }
        String username = Req.text(form.get("username"));
        Long exists = adminMapper.selectCount(new QueryWrapper<Admin>()
                .eq("username", username).ne("id", id));
        if (exists != null && exists > 0) {
            return Api.res(201, "该用户名已被占用！", List.of());
        }
        Admin admin = adminMapper.selectById(id);
        if (admin == null) return Api.res(201, "记录不存在！", List.of());

        admin.username = username;
        String password = Req.text(form.get("password"));
        if (password != null && !password.isEmpty()) {
            admin.password = encoder.encode(password);
        }
        admin.header = Req.text(form.get("imageUrl"));
        admin.power = Req.toInteger(form.get("power"));
        adminMapper.updateById(admin);
        return Api.res(200, "成功", List.of());
    }
}
