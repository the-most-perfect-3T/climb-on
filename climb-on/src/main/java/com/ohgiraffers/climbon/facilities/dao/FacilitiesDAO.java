/*
 작성자: 이승환 - 2024-11-07
    설명 :
 */
package com.ohgiraffers.climbon.facilities.dao;

import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FacilitiesDAO {
    List<FacilitiesDTO> facilitiesList();

    List<FacilitiesDTO> categorySelect(int categoryId, String facilityName);

    List<FacilitiesDTO> searchOneFacility(String code);


    FacilitiesDTO getFacility(int facilityId);


}
