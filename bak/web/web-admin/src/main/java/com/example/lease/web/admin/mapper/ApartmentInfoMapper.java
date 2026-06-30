package com.example.lease.web.admin.mapper;

import com.example.lease.model.entity.ApartmentInfo;
import com.example.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.example.lease.model.ApartmentInfo
*/
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    //查询方法，返回结果类型为IPage，参数第一个条件为IPage类型，分页插件拦截器会自动给select语句后增加limit ?,?
    IPage<ApartmentInfo> pageItem(IPage<ApartmentInfo> page, ApartmentQueryVo queryVo);
}




