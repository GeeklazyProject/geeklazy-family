package com.geeklazy.frame.starter.mybatis;

import com.alibaba.fastjson2.JSONArray;
import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author geeklazy@163.com
 * @Date 2024/2/20 11:17
 * @Description
 */
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(JSONArray.class)
public class JsonArrayTypeHandler extends BaseTypeHandler<JSONArray> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.valueOf(parameter.toJSONString()));
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String sqlJson = rs.getString(columnName);
        if (null != sqlJson){
            return JSONArray.parseArray(sqlJson);
        }
        return null;
    }

    //根据列索引，获取可以为空的结果
    @Override
    public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String sqlJson = rs.getString(columnIndex);
        if (null != sqlJson){
            return JSONArray.parseArray(sqlJson);
        }
        return null;
    }

    @Override
    public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String sqlJson = cs.getString(columnIndex);
        if (null != sqlJson) {
            return JSONArray.parseArray(sqlJson);
        }
        return null;
    }
}
