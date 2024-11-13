package com.ohgiraffers.climbon.auth.common;

import java.util.regex.Pattern;

public class NameValidator {

    // 한글 1~20자, 영문 2~20자, 숫자, 하이픈, 언더바 허용
    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣A-Za-z0-9]{1,20}$");

    public static boolean isValidName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }
}
