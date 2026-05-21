package com.example.lease.web.admin.controller.apartment;

import com.example.lease.common.result.Result;
import com.example.lease.model.entity.ApartmentInfo;
import com.example.lease.model.enums.ReleaseStatus;
import com.example.lease.web.admin.service.ApartmentInfoService;
import com.example.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.example.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.example.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentInfoController {

    @Autowired
    ApartmentInfoService apartmentInfoService;

    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo){
        apartmentInfoService.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }


    //  http://localhost:8080/admin/apartment/pageItem?current=1&size=10&provinceId=11&cityId=1101&districtId=110101
    @Operation(summary = "公寓信息分页查询")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentInfo>> pageItem(@RequestParam Integer current, @RequestParam Integer size, ApartmentQueryVo queryVo){
        IPage<ApartmentInfo> page = new Page<>(current,size);
        apartmentInfoService.pageItem(page,queryVo); //返回的Page对象就是参数page对象。
        return Result.ok(page);
    }

    @Operation(summary = "根据ID查询公寓详情信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id){
        ApartmentDetailVo apartmentDetailVo =  apartmentInfoService.getDetailById(id);
        return Result.ok(apartmentDetailVo);
    }

    @Operation(summary = "根据ID删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id){
        apartmentInfoService.removeApartmentById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status){
        apartmentInfoService.update(new LambdaUpdateWrapper<ApartmentInfo>()
                .eq(ApartmentInfo::getId,id)
                .set(ApartmentInfo::getIsRelease,status));
        return Result.ok();
    }


    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        LambdaQueryWrapper<ApartmentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApartmentInfo::getDistrictId, id);
        List<ApartmentInfo> list = apartmentInfoService.list(queryWrapper);
        return Result.ok(list);
    }
}
