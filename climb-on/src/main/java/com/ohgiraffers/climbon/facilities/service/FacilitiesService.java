package com.ohgiraffers.climbon.facilities.service;

import com.ohgiraffers.climbon.facilities.dao.FacilitiesDAO;
import com.ohgiraffers.climbon.facilities.dao.FacilityFavoriteDAO;
import com.ohgiraffers.climbon.facilities.dao.FacilityImgDAO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import com.ohgiraffers.climbon.facilities.dto.FacilityImgDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacilitiesService {

    @Autowired
    private FacilitiesDAO facilitiesDAO;
    @Autowired
    private FacilityFavoriteDAO facilityFavoriteDAO;

    @Autowired
    private FacilityImgDAO facilityImgDAO;

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

    public FacilitiesDTO getFacility(int facilityId) {
        return facilitiesDAO.getFacility(facilityId);
    }


    public int addFavorite(Integer userId, Integer facilityId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("facilityId", facilityId);

        int result = facilityFavoriteDAO.addFavorite(params);
        System.out.println("result = " + result);
        return result;
    }

    public int removeFavorite(Integer userId, Integer facilityId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("facilityId", facilityId);

        int result = facilityFavoriteDAO.removeFavorite(params);
        System.out.println("result = " + result);
        return result;
    }

    public int getIsFavorite(int id, Integer userId) {
        return facilityFavoriteDAO.getIsFavorite(id,userId);
    }


     public int getFacilityIdByName(String facilityName) {
        return facilitiesDAO.getFacilityIdByName(facilityName);
     }
    public String getFacilityNameById(int id) {
        return facilitiesDAO.getFacilityNameById(id);
    }

    public List<FacilityImgDTO> getImageById(int facilityId) {

        return facilityImgDAO.getImageById(facilityId);

    }
}
