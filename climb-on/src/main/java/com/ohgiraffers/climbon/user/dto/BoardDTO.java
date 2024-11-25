package com.ohgiraffers.climbon.user.dto;


import java.sql.Date;

public class BoardDTO {


    private Integer id;
    private int user_id;
    private int crew_code;
    private int category_code;
    private String title;
    private String content;
    private String img_url;
    private Date created_at;
    private Date updated_at;
    private int view_count;
    private boolean is_anonymous;
    /*private*/
}
