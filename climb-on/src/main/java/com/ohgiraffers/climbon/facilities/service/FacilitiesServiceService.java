package com.ohgiraffers.climbon.facilities.service;

import com.ohgiraffers.climbon.facilities.dto.FacilitiesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilitiesServiceService {
    public List<FacilitiesDTO> facilitiesList() {
        return new FacilitiesService().facilitiesList();
    }
}
