package com.example.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.lease.common.result.Result;
import com.example.lease.model.entity.LabelInfo;
import com.example.lease.model.enums.ItemType;
import com.example.lease.web.admin.service.LabelInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="标签管理")
@RestController
@RequestMapping("/admin/label")
public class LabelInfoController {

    @Autowired
    LabelInfoService labelInfoService;

    // 查列表
    @Operation(summary = "查询标签列表(根据类型)")
    @GetMapping("list")
    public Result<List<LabelInfo>> list(@RequestParam(required = false) ItemType type) {
        LambdaQueryWrapper<LabelInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type!=null,LabelInfo::getType,type);
        List<LabelInfo> list = labelInfoService.list(wrapper);
        return Result.ok(list);
    }
    // 保存更新
    @Operation(summary = "保存或更新标签")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LabelInfo labelInfo) {
        labelInfoService.saveOrUpdate(labelInfo);
        return Result.ok();
    }

    // 根据id删标签
    @Operation(summary = "根据ID删除标签")
    @DeleteMapping("deleteById")
    public Result deleteById(@RequestParam long id) {
        labelInfoService.removeById(id);
        return Result.ok();
    }

}
