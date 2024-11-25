package com.ohgiraffers.climbon.auth.model.dto;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    /** "kakao_account" 내에 email 정보가 있는지 확인 후 반환*/
    @Override
    public String getEmail() {
        Object object = attributes.get("kakao_account");
        if (object instanceof Map) {
            Map<String, Object> accountMap = (Map<String, Object>) object;
            return (String) accountMap.get("email");
        }
        return null;
    }

    /**"properties" 내에 nickname 정보가 있는지 확인 후 반환*/
    @Override
    public String getName() {
        Object properties = attributes.get("properties");
        if (properties instanceof Map) {
            Map<String, Object> propertiesMap = (Map<String, Object>) properties;
            return (String) propertiesMap.get("nickname");
        }
        return null;
    }

    /**properties 내에 profile_img 정보가 있는지 확인 후 반환*/
    @Override
    public String getPictureUrl() {
        Object properties = attributes.get("properties");
        if (properties instanceof Map) {
            Map<String, Object> propertiesMap = (Map<String, Object>) properties;
            return (String) propertiesMap.get("profile_image");
        }
        return null;
    }
}
