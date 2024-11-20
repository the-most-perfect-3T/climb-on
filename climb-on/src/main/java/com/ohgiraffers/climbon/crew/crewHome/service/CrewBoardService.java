package com.ohgiraffers.climbon.crew.crewHome.service;

import com.ohgiraffers.climbon.crew.crewHome.dao.CrewBoardDAO;
import com.ohgiraffers.climbon.crew.crewHome.dto.CrewBoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrewBoardService {

    @Autowired
    private CrewBoardDAO crewBoardDAO;


    public int insertPost(CrewBoardDTO crewBoardDTO) {
        int result = crewBoardDAO.insertPost(crewBoardDTO);
        return result;

    }

    public CrewBoardDTO selectLastPost() {
        return crewBoardDAO.selectLastPost();
    }


    public Integer getCrewCode(int id) {
        return crewBoardDAO.getCrewCode(id);
    }

    public CrewBoardDTO selectOnePostById(int id) {
        return crewBoardDAO.selectOnePostById(id);
    }

    public int updatePostById(CrewBoardDTO crewBoardDTO) {
        return crewBoardDAO.updatePostById(crewBoardDTO);
    }
}
