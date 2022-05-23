package com.zjy.dao.vo;

import com.zjy.entity.model.UpgradeLog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpgradeLogVo extends UpgradeLog {
    private List<UpgradeLogItem> contentList;
}
