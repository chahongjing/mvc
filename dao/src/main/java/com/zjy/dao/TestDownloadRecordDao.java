package com.zjy.dao;

import com.zjy.entity.model.TestDownloadRecord;

import java.util.List;

public interface TestDownloadRecordDao {
    int insert(TestDownloadRecord record);
    List<TestDownloadRecord> queryList();
}
