package com.fans.controller;

import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.config.AppProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/** 图片上传：二维码、头像等，返回可直接访问的 URL */
@RestController
public class UploadController {

    private static final List<String> ALLOWED_EXT = List.of("png", "jpg", "jpeg", "gif", "webp");

    private final AppProperties props;

    public UploadController(AppProperties props) {
        this.props = props;
    }

    @PostMapping("/upload/image")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (CurrentUser.id() == null) {
            return Api.res(401, "请先登录", List.of());
        }
        if (file == null || file.isEmpty()) {
            return Api.res(400, "请选择图片", List.of());
        }
        if (file.getSize() > props.getUpload().getMaxSizeMb() * 1024 * 1024) {
            return Api.res(400, "图片不能超过 " + props.getUpload().getMaxSizeMb() + "MB", List.of());
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType();
        if (!contentType.startsWith("image/")) {
            return Api.res(400, "只允许上传图片", List.of());
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = original.contains(".")
                ? original.substring(original.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT)
                : "";
        if (!ALLOWED_EXT.contains(ext)) {
            return Api.res(400, "仅支持 png/jpg/jpeg/gif/webp", List.of());
        }

        Path dir = Paths.get(props.getUpload().getDir()).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        String name = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path target = dir.resolve(name);
        try (var in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("url", "/uploads/" + name);
        data.put("name", name);
        data.put("size", file.getSize());
        return new LinkedHashMap<>(Api.res(200, "上传成功", data));
    }
}
