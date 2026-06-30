package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.LabelInfo;
import com.seig.rental.model.service.LabelInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-标签管理")
@RestController
@RequestMapping("/label")
public class LabelInfoController {

    @Resource
    private LabelInfoService labelInfoService;

    @Operation(summary = "根据类型查询标签列表")
    @GetMapping("/list")
    public Result<List<LabelInfo>> list(
            @Parameter(description = "标签类型: room / apartment") @RequestParam(defaultValue = "room") String type) {
        return Result.ok(labelInfoService.listByType(type));
    }

    @Operation(summary = "保存或更新标签")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody LabelInfo labelInfo) {
        labelInfoService.saveOrUpdate(labelInfo);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除标签")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        labelInfoService.removeById(id);
        return Result.ok();
    }
}
