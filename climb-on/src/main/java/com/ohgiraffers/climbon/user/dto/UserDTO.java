package com.ohgiraffers.climbon.user.dto;

import com.ohgiraffers.climbon.user.Enum.UserType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserDTO {
    private Integer id;                         // 사용자 고유 ID (Integer로 변경)
    private String nickname;                    // 사용자 닉네임
    private String userId;                      // 사용자 고유 아이디
    private String password;                    // 사용자 비밀번호 (해시 처리된 값)
    private String userRole;                    // 사용자 역할 (예: 'user', 'admin')
    private String profilePic;                  // 사용자 프로필 사진 URL
    private int status;                     // 사용자 계정 상태 (0: 비활성, 1: 활성)
    private Boolean isBusiness;                 // 사용자 계정이 비즈니스인지 여부
    private LocalDateTime createdAt;            // 계정 생성 시간
    private LocalDateTime inactiveAt;           // 계정 비활성화 시간
    private Boolean userFacilityFavorite;       // 사용자가 시설에 '좋아요'를 눌렀는지 여부
    private Boolean userReviewHeart;            // 사용자가 리뷰에 '하트'를 눌렀는지 여부
}
