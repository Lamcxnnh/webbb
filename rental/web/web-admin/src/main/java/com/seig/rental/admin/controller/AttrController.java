package com.seig.rental.admin.controller;

import com.seig.rental.common.base.Result;
import com.seig.rental.model.entity.AttrKey;
import com.seig.rental.model.entity.AttrValue;
import com.seig.rental.model.service.AttrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台-属性管理")
@RestController
@RequestMapping("/attr")
public class AttrController {

    @Resource
    private AttrService attrService;

    // ===== 属性名称 =====

    @Operation(summary = "查询全部属性名称")
    @GetMapping("/key/list")
    public Result<List<AttrKey>> listKeys() {
        return Result.ok(attrService.listKeys());
    }

    @Operation(summary = "保存或更新属性名称")
    @PostMapping("/key/save")
    public Result<Void> saveKey(@RequestBody AttrKey attrKey) {
        attrService.saveOrUpdateKey(attrKey);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除属性名称（同时删除其属性值）")
    @DeleteMapping("/key/{id}")
    public Result<Void> deleteKey(@PathVariable Long id) {
        attrService.deleteKey(id);
        return Result.ok();
    }

    // ===== 属性值 =====

    @Operation(summary = "根据属性名称ID查询属性值列表")
    @GetMapping("/value/list")
    public Result<List<AttrValue>> listValues(@RequestParam Long keyId) {
        return Result.ok(attrService.listValuesByKeyId(keyId));
    }

    @Operation(summary = "保存或更新属性值")
    @PostMapping("/value/save")
    public Result<Void> saveValue(@RequestBody AttrValue attrValue) {
        attrService.saveOrUpdateValue(attrValue);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除属性值")
    @DeleteMapping("/value/{id}")
    public Result<Void> deleteValue(@PathVariable Long id) {
        attrService.deleteValue(id);
        return Result.ok();
    }
}
