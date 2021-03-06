package com.zjy.dao.common.multiDataSource;

import com.zjy.baseframework.enums.YesNo;
import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.entity.enums.DownTaskStatus;
import com.zjy.entity.enums.LogLevel;
import com.zjy.entity.enums.PermissionType;
import com.zjy.entity.enums.PsermissionIncludeType;
import com.zjy.entity.enums.Sex;
import com.zjy.entity.enums.UserStatus;
import com.zjy.entity.enums.UserTypeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes({DownTaskStatus.class, Sex.class, UserStatus.class, YesNo.class, LogLevel.class, UserTypeEnum.class,
        PermissionType.class, PsermissionIncludeType.class})
public class CodeEnumTypeHandler<E extends Enum<E> & IBaseEnum> extends BaseTypeHandler<IBaseEnum> {

    private Class<E> type;

    public CodeEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IBaseEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        if (rs.wasNull()) {
//            return null;
//        }
        int i = rs.getInt(columnName);
        return getTypeValue(i);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        if (rs.wasNull()) {
//            return null;
//        }
        int i = rs.getInt(columnIndex);
        return getTypeValue(i);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.wasNull()) {
            return null;
        }
        int i = cs.getInt(columnIndex);
        return getTypeValue(i);
    }

    private E getTypeValue(int val) {
        try {
            return IBaseEnum.getByValue(type, val);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot convert " + val + " to " + type.getSimpleName() + " by ordinal value.", ex);
        }
    }

    public static void registerTypeHandle(TypeHandlerRegistry typeHandlerRegistry, List<Class> classList) {
        for (Class aClass : classList) {
            // ?????????????????????IBaseCodeEnum??????, ???MybatisFieldEnum??????
            if (aClass.isEnum() && IBaseEnum.class.isAssignableFrom(aClass)) {
                // ??????
                try {
                    typeHandlerRegistry.register(aClass.getTypeName(), CodeEnumTypeHandler.class.getTypeName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}