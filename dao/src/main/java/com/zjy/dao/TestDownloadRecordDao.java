package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.entity.model.TestDownloadRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestDownloadRecordDao extends BaseDao<TestDownloadRecord> {
    List<TestDownloadRecord> queryList();
}
