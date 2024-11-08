package com.ohgiraffers.climbon.facilities.service;

import com.ohgiraffers.climbon.facilities.dao.FacilitiesDAO;
import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class FacilitiesService {

    @Autowired
    private FacilitiesDAO facilitiesDAO;

    public List<FacilitiesDTO> facilitiesList() {

        List<FacilitiesDTO> facilitiesDTOList = facilitiesDAO.facilitiesList();

        return facilitiesDTOList;
    }
}
