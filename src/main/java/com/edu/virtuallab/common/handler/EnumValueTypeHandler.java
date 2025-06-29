package com.edu.virtuallab.common.handler;

import com.edu.virtuallab.common.enums.NotificationType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumValueTypeHandler extends BaseTypeHandler<NotificationType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    NotificationType parameter, JdbcType jdbcType) throws SQLException {
        // 使用枚举的 getValue() 方法存储到数据库
        ps.setString(i, parameter.getValue());
    }

    @Override
    public NotificationType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return convert(value);
    }

    @Override
    public NotificationType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return convert(value);
    }

    @Override
    public NotificationType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return convert(value);
    }

    // 统一转换方法
    private NotificationType convert(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return NotificationType.fromValue(value);
    }
}
