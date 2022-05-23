package com.zjy.dao.vo;

import com.zjy.entity.model.OperateLog;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OperateLogVo extends OperateLog {
    private Date beginDate;

    private Date endDate;

    private String userName;
}
