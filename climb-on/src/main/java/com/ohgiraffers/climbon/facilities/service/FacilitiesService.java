package com.ohgiraffers.climbon.facilities.service;

import com.ohgiraffers.climbon.facilities.dao.FacilitiesDAO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FacilitiesService {

    @Autowired
    private FacilitiesDAO facilitiesDAO;

    public List<FacilitiesDTO> facilitiesList() {

        List<FacilitiesDTO> facilitiesDTOList = facilitiesDAO.facilitiesList();

        return facilitiesDTOList;
    }

    public List<FacilitiesDTO> categorySelect(int categoryId,String facilityName) {

        List<FacilitiesDTO> facilitiesDTOList = facilitiesDAO.categorySelect(categoryId,facilityName);

        return facilitiesDTOList;
    }

    public List<FacilitiesDTO> searchOneFacility(String code) {
        List<FacilitiesDTO> facilities =facilitiesDAO.searchOneFacility(code);
        return facilities;
    }

    public List<FacilitiesDTO> getFacilitySuggestions(String code) {
        return facilitiesDAO.searchOneFacility(code);
    }
}
