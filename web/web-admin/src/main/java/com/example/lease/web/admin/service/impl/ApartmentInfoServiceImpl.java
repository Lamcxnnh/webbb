package com.example.lease.web.admin.service.impl;

import com.example.lease.common.exception.LeaseException;
import com.example.lease.common.result.ResultCodeEnum;
import com.example.lease.model.entity.*;
import com.example.lease.model.enums.ItemType;
import com.example.lease.model.enums.ReleaseStatus;
import com.example.lease.web.admin.mapper.*;
import com.example.lease.web.admin.service.*;
import com.example.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.example.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.example.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.example.lease.web.admin.vo.fee.FeeValueVo;
import com.example.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    GraphInfoService graphInfoService;

    @Autowired
    ApartmentFacilityService apartmentFacilityService; //公寓配套中间表业务接口

    @Autowired
    ApartmentLabelService apartmentLabelService; //公寓标签中间表业务接口

    @Autowired
    ApartmentFeeValueService apartmentFeeValueService; //公寓杂费中间表业务接口


    @Autowired
    FacilityInfoService facilityInfoService;

    @Autowired
    LabelInfoService labelInfoService;

    @Autowired
    FeeValueService feeValueService;

    @Autowired
    ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    GraphInfoMapper graphInfoMapper;

    @Autowired
    FacilityInfoMapper facilityInfoMapper;

    @Autowired
    LabelInfoMapper labelInfoMapper;

    @Autowired
    FeeValueMapper feeValueMapper;

    @Autowired
    RoomInfoService roomInfoService;


    @Autowired
    ProvinceInfoService provinceInfoService;

    @Autowired
    CityInfoService cityInfoService;

    @Autowired
    DistrictInfoService districtInfoService;

    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {

        //自己封装省、市、区的名称
        apartmentSubmitVo.setProvinceName(provinceInfoService.getById(apartmentSubmitVo.getProvinceId()).getName());
        apartmentSubmitVo.setCityName(cityInfoService.getById(apartmentSubmitVo.getCityId()).getName());
        apartmentSubmitVo.setDistrictName(districtInfoService.getById(apartmentSubmitVo.getDistrictId()).getName());

        //1.保存或更新主表数据：公寓信息表
        this.saveOrUpdate(apartmentSubmitVo); // VO对象没有转换为实体对象进行保存也是OK的。
        Long id = apartmentSubmitVo.getId(); //公寓id
        boolean isUpdate = id != null;
        if (isUpdate) {//  isUpdate=true 修改
            //2.对于修改来讲，先删除旧的关系数据，再保存新的关系数据（简化关系操作）。(关系变动 ：存在  不变的，新增的，删除的)

            //2.1.删除图片关系数据
//            LambdaQueryWrapper<GraphInfo> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
//            queryWrapper.eq(GraphInfo::getItemId,id);
//            graphInfoService.remove(queryWrapper);
            graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>()
                    .eq(GraphInfo::getItemType, ItemType.APARTMENT).eq(GraphInfo::getItemId, id));

            //2.2.删除公寓配套关系数据
            apartmentFacilityService.remove(new LambdaQueryWrapper<ApartmentFacility>()
                    .eq(ApartmentFacility::getApartmentId, id));


            //2.3.删除公寓标签关系数据
            apartmentLabelService.remove(new LambdaQueryWrapper<ApartmentLabel>()
                    .eq(ApartmentLabel::getApartmentId, id));

            //2.4.删除公寓杂费关系数据
            apartmentFeeValueService.remove(new LambdaQueryWrapper<ApartmentFeeValue>()
                    .eq(ApartmentFeeValue::getApartmentId, id));

        }

        //isUpdate=false 添加
        //3.保存图片关系数据     1-多
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            List<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setName(graphVo.getName());
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(id);
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }

        //4.保存公寓配套关系数据
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            List<ApartmentFacility> apartmentFacilityList = new ArrayList<>(facilityInfoIds.size());
            for (Long facilityInfoId : facilityInfoIds) {
//                ApartmentFacility apartmentFacility = new ApartmentFacility();
//                apartmentFacility.setApartmentId(id);
//                apartmentFacility.setFacilityId(facilityInfoId);
                ApartmentFacility apartmentFacility = ApartmentFacility.builder()
                        .apartmentId(id).facilityId(facilityInfoId)
                        .build();
                apartmentFacilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(apartmentFacilityList);
        }

        //5.保存公寓标签关系数据
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>(labelIds.size());
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = ApartmentLabel.builder()
                        .apartmentId(id).labelId(labelId).build();
                apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabelList);
        }

        //6.保存公寓杂费关系数据
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            List<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>(feeValueIds.size());
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder()
                        .apartmentId(id).feeValueId(feeValueId).build();
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }
    }

    @Override
    public IPage<ApartmentInfo> pageItem(IPage<ApartmentInfo> page, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.pageItem(page, queryVo);
    }

    /**
     * 根据ID查询公寓详情数据
     *
     * @param id 公寓主键
     * @return
     */
    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        //1.查询公寓表数据
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        if (apartmentInfo == null) {
            return null;
        }
        //2.查询公寓图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectGraphByItemTypeAndItemId(ItemType.APARTMENT, id);

        //3.查询公寓标签列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectLabelInfoByApartmentId(ItemType.APARTMENT, id);

        //4.查询公寓配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectFacilityInfoByApartmentId(ItemType.APARTMENT, id);

        //5.查询公寓杂费列表
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectFeeValueByApartmentId(id);

        //6.封装数据
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);

        //7.返回数据
        return apartmentDetailVo;
    }


    @Override
    public void removeApartmentById(Long id) {
        //1.删除公寓
        //公寓存在房间不能删除的？？？？？？？？？？？？？？？？
        long count = roomInfoService.count(new LambdaQueryWrapper<RoomInfo>()
                .eq(RoomInfo::getApartmentId, id));
        if (count > 0) {
            throw new LeaseException(ResultCodeEnum.DELETE_ERROR);
        }


        //2.删除公寓图片
        graphInfoService.remove(new LambdaQueryWrapper<GraphInfo>()
                .eq(GraphInfo::getItemType, ItemType.APARTMENT)
                .eq(GraphInfo::getItemId, id));

        //3.删除公寓配套
        apartmentFacilityService.remove(new LambdaQueryWrapper<ApartmentFacility>()
                .eq(ApartmentFacility::getApartmentId, id));

        //4.删除公寓标签
        apartmentLabelService.remove(new LambdaQueryWrapper<ApartmentLabel>()
                .eq(ApartmentLabel::getApartmentId, id));

        //5.删除公寓杂费
        apartmentFeeValueService.remove(new LambdaQueryWrapper<ApartmentFeeValue>()
                .eq(ApartmentFeeValue::getApartmentId, id));

        //推荐先删除关联表数据，再删除主表数据。这样不用管数据库的物理主外键是否存在。
        super.removeById(id);
    }

}




