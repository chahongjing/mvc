package com.zjy.service.request;

import com.zjy.service.common.PageInfomation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KvConfigLogRequest extends PageInfomation {
    private Long kvId;
}
