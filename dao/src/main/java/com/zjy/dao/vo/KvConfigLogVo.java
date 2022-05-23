package com.zjy.dao.vo;

import com.zjy.entity.model.KvConfigLog;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KvConfigLogVo extends KvConfigLog {
    private String createByName;
}
