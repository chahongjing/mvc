package com.zjy.service.request;

import com.zjy.entity.enums.OrderByType;
import com.zjy.entity.enums.Sex;
import com.zjy.service.common.PageInfomation;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chahongjing
 * @create 2016-12-27 21:10
 */
@Setter
@Getter
public class UserInfoRequest extends PageInfomation {
    private String userName;
    private Sex sex;
    private OrderByType nameOrderBy;
    private OrderByType codeOrderBy;
    private OrderByType createdOnOrderBy;
}
