package com.example.lease.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.lease.model.entity.ApartmentInfo;
import com.example.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.example.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.example.lease.web.admin.vo.apartment.ApartmentSubmitVo;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ApartmentInfoService extends IService<ApartmentInfo> {

    /**
     * 保存或更新公寓信息
     * @param apartmentSubmitVo
     */
    void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);

    /**
     * 公寓信息分页查询
     * @param page 分页条件
     * @param queryVo 查询条件
     * return 分页对象
     */
    IPage<ApartmentInfo> pageItem(IPage<ApartmentInfo> page, ApartmentQueryVo queryVo);

    /**
     * 根据ID查询公寓详细信息
     * @param id 公寓主键
     * @return 封装公寓详细信息
     */
    ApartmentDetailVo getDetailById(Long id);

    /**
     * 根据ID删除公寓信息
     * @param id 公寓主键
     */
    void removeApartmentById(Long id);

}
