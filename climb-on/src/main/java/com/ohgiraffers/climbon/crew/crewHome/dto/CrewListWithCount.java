package com.ohgiraffers.climbon.crew.crewHome.dto;

import java.util.List;

public class CrewListWithCount {

    private Integer count;
    private List<CrewDTO> crews;

    public CrewListWithCount() {
    }

    public CrewListWithCount(Integer count, List<CrewDTO> crews) {
        this.count = count;
        this.crews = crews;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CrewDTO> getCrews() {
        return crews;
    }

    public void setCrews(List<CrewDTO> crews) {
        this.crews = crews;
    }

    @Override
    public String toString() {
        return "CrewListWithCount{" +
                "count=" + count +
                ", crews=" + crews +
                '}';
    }
}
