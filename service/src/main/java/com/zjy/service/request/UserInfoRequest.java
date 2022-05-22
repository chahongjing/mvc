package com.zjy.service.request;

import com.zjy.entity.enums.OrderByType;
import com.zjy.entity.enums.Sex;
import com.zjy.service.common.PageInfomation;

/**
 * @author chahongjing
 * @create 2016-12-27 21:10
 */
public class UserInfoRequest extends PageInfomation {
    private String userName;
    private Sex sex;
    private OrderByType nameOrderBy;
    private OrderByType codeOrderBy;
    private OrderByType createdOnOrderBy;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public OrderByType getNameOrderBy() {
        return nameOrderBy;
    }

    public void setNameOrderBy(OrderByType nameOrderBy) {
        this.nameOrderBy = nameOrderBy;
    }

    public OrderByType getCodeOrderBy() {
        return codeOrderBy;
    }

    public void setCodeOrderBy(OrderByType codeOrderBy) {
        this.codeOrderBy = codeOrderBy;
    }

    public OrderByType getCreatedOnOrderBy() {
        return createdOnOrderBy;
    }

    public void setCreatedOnOrderBy(OrderByType createdOnOrderBy) {
        this.createdOnOrderBy = createdOnOrderBy;
    }
}
