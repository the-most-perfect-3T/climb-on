/*
 작성자: 이승환 - 2024-11-07
    설명 :Enum핸들러
 */
package com.ohgiraffers.climbon.facilities.handler;

import com.ohgiraffers.climbon.facilities.Enum.FacilityType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FacilityTypeTypeHandler extends BaseTypeHandler<FacilityType> {

    // PreparedStatement에 enum 값을 설정할 때 사용 (Enum -> DB 값)
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, FacilityType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name()); // Enum을 String으로 변환하여 설정
    }

    // ResultSet에서 값을 읽을 때 사용 (DB 값 -> Enum)
    @Override
    public FacilityType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : FacilityType.valueOf(value); // DB 값을 Enum으로 변환
    }

    @Override
    public FacilityType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : FacilityType.valueOf(value); // DB 값을 Enum으로 변환
    }

    @Override
    public FacilityType getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : FacilityType.valueOf(value); // DB 값을 Enum으로 변환
    }
}
