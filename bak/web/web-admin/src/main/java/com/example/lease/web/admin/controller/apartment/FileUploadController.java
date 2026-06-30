package com.example.lease.web.admin.controller.apartment;

import com.example.lease.common.result.Result;
import com.example.lease.web.admin.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/admin/file")
public class FileUploadController {
    @Autowired
    FileService fileService;

    @Operation(summary = "文件上传")
    @PostMapping("upload")
    public Result<String> upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        String url = fileService.upload(multipartFile);
        return Result.ok(url);  //  200  “成功”
    }
}
