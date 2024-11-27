package com.ohgiraffers.climbon.crew.crewHome.service;

import com.ohgiraffers.climbon.crew.crewHome.dao.CrewDAO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    public List<CrewDTO> selectFiveCrews(int page, String sort, List<String> areas) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("sort", sort);
        map.put("areas", areas);
        return crewDAO.selectFiveCrews(map);
    }

    public int countAllCrews() {
        return crewDAO.countAllCrews();
    }

    public List<CrewDTO> selectCrews() {
        return crewDAO.selectCrews();
    }

    public int countCrewsFilteredByAreas(List<String> areas) {
        return crewDAO.countCrewsFilteredByAreas(areas);
    }

    public List<CrewDTO> getRecruitingCrews() {
        return crewDAO.getRecruitingCrews();
    }

    public Object hasCrew(Integer id) {
        return crewDAO.hasCrew(id);
    }

    public int getCrewCodeFromCrewsByUserId(Integer id) {
        return crewDAO.getCrewCodeFromCrewsByUserId(id);
    }

    public int crewCaptainInsert(Integer id, int crewCode) {
        return crewDAO.crewCaptainInsert(id, crewCode);
    }


}
