package com.ohgiraffers.climbon.crew.crewHome.service;

import com.ohgiraffers.climbon.crew.crewHome.dao.CrewDAO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CrewService {

    @Autowired
    CrewDAO crewDAO;


    /**crewName이 중복되는지 확인
     * @return boolean
     * */
    public boolean isCrewNameExists(String crewName) {
        CrewDTO crewDTO = crewDAO.isCrewNameExists(crewName);
        if(Objects.isNull(crewDTO)){
            return false;
        }
        return true;
    }

    /**crew 등록
     *
    * */
    public int registerCrew(CrewDTO crewDTO) {
        return crewDAO.registerCrew(crewDTO);
    }
}
