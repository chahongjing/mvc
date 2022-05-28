package com.zjy.dao.vo;

import com.zjy.entity.enums.PermissionType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PermissionCheckVo {
    private Long id;
    private String name;
    private boolean isCheck;
    private PermissionType type;
    private boolean showDetail;
    // 关联业务id，如角色id，用户id
    private Long relatedId;
    private String code;
    List<PermissionCheckVo> subList;

    public PermissionCheckVo() {
        subList = new ArrayList<>();
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    public boolean getIsCheck() {
        return this.isCheck;
    }
}
