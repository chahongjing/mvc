package com.zjy.common.utils;

import com.zjy.baseframework.common.Constants;
import com.zjy.baseframework.common.DownloadException;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.FileSuffix;
import com.zjy.common.common.ExcelHeader;
import com.zjy.common.common.ExcelRowData;
import com.zjy.common.enums.HandleImportErrorType;
import com.zjy.common.enums.ImportExcelDataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel数据导入
 * @param <T>
 */
@Slf4j
public abstract class AbstractImportExcel<T extends ExcelRowData> {
    @Autowired
    protected JsonUtils jsonUtils;

    protected ImportExcelDataType importExcelDataType = ImportExcelDataType.IMPORT_CORRECT_DATA;
    protected HandleImportErrorType handleImportErrorType = HandleImportErrorType.RETURN_ERROR_MESSAGE;

    public void setImportExcelDataType(ImportExcelDataType importExcelDataType) {
        this.importExcelDataType = importExcelDataType;
    }
    public void setHandleImportErrorType(HandleImportErrorType handleImportErrorType) {
        this.handleImportErrorType = handleImportErrorType;
    }

    /**
     * 导入excel
     * @param is
     * @param sheetName
     * @param clazz
     * @param headers
     */
    public List<String> importExcel(InputStream is, String sheetName, Class<T> clazz, List<ExcelHeader> headers){
        return importExcel(is, sheetName, clazz, headers, null);
    }

    /**
     * 导入excel
     * @param is
     * @param sheetName
     * @param clazz
     * @param headers
     * @param response
     */
    public List<String> importExcel(InputStream is, String sheetName, Class<T> clazz, List<ExcelHeader> headers, HttpServletResponse response){
        // read excel
        List<T> excelRowData = ExcelUtils.excelToList(is, sheetName, clazz, headers);
        checkRow(excelRowData);

        List<T> correctDataList = new ArrayList<>();
        List<T> errorDataList = new ArrayList<>();
        for (T row : excelRowData) {
            if(row.getErrorMsg() == null || row.getErrorMsg().size() == 0) {
                correctDataList.add(row);
            } else {
                errorDataList.add(row);
            }
        }
        List<String> errorMsgList = handleErrorRow(errorDataList, headers, sheetName, response);
        // 没有异常数据或正确的数据下导入
        if (correctDataList.size() > 0 && importExcelDataType == ImportExcelDataType.IMPORT_CORRECT_DATA) {
            doImport(correctDataList);
        }
        return errorMsgList;
    }

    /**
     * 检查每行数据是否满足业务要求
     * @param list
     */
    protected void checkRow(List<T> list){
    }

    /**
     * 处理异常业务数据。1返回错误信息；2下载错误信息
     * @param list
     * @param headers
     * @param sheetName
     * @param response
     */
    protected List<String> handleErrorRow(List<T> list, List<ExcelHeader> headers, String sheetName, HttpServletResponse response){
        List<String> errorList = new ArrayList<>();
        if(list == null || list.size() == 0) {
            return errorList;
        }
        if(handleImportErrorType == HandleImportErrorType.RETURN_ERROR_MESSAGE) {
            StringBuilder sb = new StringBuilder();
            for (T errRow : list) {
                errorList.add(String.join(";", errRow.getErrorMsg()));
            }
        } else {
            Workbook workbook = ExcelUtils.createWorkbook(FileSuffix.XLS);

            List<ExcelHeader> newHeaders = new ArrayList<>(headers);
            CellStyle cellStyle = workbook.createCellStyle();
            Font cellFont = workbook.createFont();
            cellFont.setColor(IndexedColors.RED.index);
            cellStyle.setFont(cellFont);
            ExcelHeader errorHeader = new ExcelHeader("errorMsg", "错误信息");
            errorHeader.setCellStyle(cellStyle);
            newHeaders.add(errorHeader);

            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                // 生成excel
                ExcelUtils.listToExcel(workbook, list, newHeaders, sheetName);
                workbook.write(os);
                os.flush();
                try (ByteArrayInputStream swapStream = new ByteArrayInputStream(os.toByteArray())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    // 下载excel
                    DownloadUtils.download(swapStream, "导入的错误数据" + sdf.format(new Date()) + "." + FileSuffix.XLS.getCode(), response);
                } catch (Exception e) {
                    throw new DownloadException("下载出错！", e);
                }
            } catch (Exception e) {
                throw new DownloadException("生成excel出错！", e);
            }
        }
        return errorList;
    }

    /**
     * 格式正确的数据处理，如写入数据库
     * @param list
     */
    protected void doImport(List<T> list){

    }
}
