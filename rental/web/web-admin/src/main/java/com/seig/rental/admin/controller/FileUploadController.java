package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.common.utils.MinioUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "后台-图片上传")
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Resource
    private MinioUtil minioUtil;

    @Operation(summary = "上传单张图片")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,
                                  @RequestParam(defaultValue = "common") String dir) {
        String url = minioUtil.upload(file, dir);
        return Result.ok(url);
    }

    @Operation(summary = "批量上传图片")
    @PostMapping("/upload/batch")
    public Result<List<String>> uploadBatch(@RequestParam("files") List<MultipartFile> files,
                                             @RequestParam(defaultValue = "common") String dir) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(minioUtil.upload(file, dir));
        }
        return Result.ok(urls);
    }
}
