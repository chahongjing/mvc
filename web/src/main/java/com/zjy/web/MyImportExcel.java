package com.zjy.web;

import com.zjy.common.enums.HandleImportErrorType;
import com.zjy.common.enums.ImportExcelDataType;
import com.zjy.common.utils.AbstractImportExcel;
import com.zjy.dao.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class MyImportExcel extends AbstractImportExcel<UserInfoVo> {

    @PostConstruct
    public void init() {
        this.handleImportErrorType = HandleImportErrorType.DOWNLOAD_EXCEL_WITH_MESSAGE;
        this.importExcelDataType = ImportExcelDataType.INTERUPT_WHEN_ERROR;
    }
    @Override
    protected void checkRow(List<UserInfoVo> list) {
    }

    @Override
    protected void doImport(List<UserInfoVo> list) {
        if(list == null || list.size() == 0) {
            return;
        }
        for (UserInfoVo data : list) {
            log.info("insert into data: {}", jsonUtils.toJSON(data));
        }
    }


}
