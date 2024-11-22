package com.ohgiraffers.climbon.facilities.dao;

import com.ohgiraffers.climbon.facilities.dto.FacilityImgDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FacilityImgDAO {
    List<FacilityImgDTO> getImageById(int facilityId);
}
