package com.zjy.common.utils;

import com.zjy.baseframework.enums.FileSuffix;
import com.zjy.baseframework.interfaces.IBaseEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * apache poi, 阿里easyexcel
 *
 * @author: zjy
 * @date: 2019-11-24 20:35:48
 */
public class ExcelUtils {

    public static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {
    }

    // region 变量
    private static final int XLSMaxSheetRow = 65536;
    private static final int XLSXMaxSheetRow = 1000000;//1048576
    // endregion

    // region excle转list
    public static <T> List<T> excelToList(InputStream in, String sheetName, Class<T> clazz, List<ExcelHeader> headers) throws Exception{
        List<T> list = new ArrayList<>();
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(in);
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("在excel中未找到对应的sheet:" + sheetName);
        }

        Row row;
        Cell cell;
        String fieldName;
        // 处理列和字段对应关系
        row = sheet.getRow(0);
        if (row == null) return list;
        for (ExcelHeader header : headers) {
            fieldName = StringUtils.EMPTY;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                cell = row.getCell(i);
                if (StringUtils.isBlank(cell.getStringCellValue())) continue;
                if (cell.getStringCellValue().trim().equals(header.getName())) {
                    fieldName = header.getFieldName();
                    header.setColumnIndex(i);
                }
            }
            if (StringUtils.isBlank(fieldName)) {
                throw new IllegalArgumentException("未找到列名【" + header.getFieldName() + "-" + header.getName() + "】");
            }
        }
        // 将行转为对象
        T entity;
        Object cellValue;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            //新建要转换的对象
            try {
                entity = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return list;
            }
            row = sheet.getRow(i);
            //给对象中的字段赋值
            for (ExcelHeader header : headers) {
                if (header.getColumnIndex() < 0) continue;
                cell = row.getCell(header.getColumnIndex());
                cellValue = getCellValue(cell);
                // 给对象赋值
                try {
                    setFieldValueByName(header.getFieldName(), cellValue, entity);
                } catch (Exception e) {
                    log.error(String.format("excel的 %s 页签第 %d 行第 %s 列的内容不正确:%s！", sheet.getSheetName(), i, header.getName(), cellValue));
                }
            }
            list.add(entity);
        }
        return list;
    }
    // endregion

    // region list转excel
    @Deprecated
    public static <T> void listToExcelOld(List<T> list, Map<String, String> headers, String sheetName, String filePath) {
        LinkedHashMap<String, List<T>> map = new LinkedHashMap<>();
        map.put(sheetName, list);
        listToExcelOld(map, headers, filePath);
    }

    @Deprecated
    public static <T> void listToExcelOld(Map<String, List<T>> sheetList, Map<String, String> headers, String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("filePath不能为空！");
        }
        Workbook workbook;
        try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(filePath))) {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.mkdirs();
            }
            if (filePath.toLowerCase().endsWith(FileSuffix.XLSX.getCode())) {
                workbook = new XSSFWorkbook();
            } else {
                workbook = new HSSFWorkbook();
            }

            for (Map.Entry<String, List<T>> entry : sheetList.entrySet()) {
                listToExcelOld(workbook, entry.getValue(), headers, entry.getKey());
            }
            os.flush();
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static <T> void listToExcelOld(List<T> list, Map<String, String> headers, String sheetName, OutputStream os, FileSuffix suffix) {
        Workbook workbook = createWorkbook(suffix);
        try {
            listToExcelOld(workbook, list, headers, sheetName);
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void listToExcel(List<T> list, List<ExcelHeader> headers, String sheetName, OutputStream os, FileSuffix suffix) {
        Workbook workbook = createWorkbook(suffix);
        try {
            listToExcel(workbook, list, headers, sheetName, 0, 1);
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 多表头写入excel
     * @Author: fei.wei
     * @Date: 2020/8/12 11:13
     * @param multiHeaderVOList
     * @param sheetName
     * @param os
     * @param suffix
     * @Return:
     */
    public static <T> void listToMultiHeaderExcel(List<MultiHeaderVO> multiHeaderVOList, String sheetName, OutputStream os, FileSuffix suffix) {
        Workbook workbook = createWorkbook(suffix);
        try {
            int rowNum = 0;

            for(MultiHeaderVO vo : multiHeaderVOList){
                listToMultiHeaderExcel(workbook,vo.getData(),vo.getHeader(),sheetName,rowNum,0);
                rowNum = vo.getData().size()+1+rowNum;  // 多表头时，计算表头所占一行
            }
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 创建不同版本的Workbook
     * @Author: fei.wei
     * @Date: 2020/8/12 11:15
     * @param suffix
     * @Return:
     */
    private static Workbook createWorkbook(FileSuffix suffix) {
        Workbook workbook;
        if (suffix != null && suffix.equals(FileSuffix.XLSX)) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        return workbook;
    }

    @Deprecated
    public static <T> void listToExcelOld(Workbook workbook, List<T> list, Map<String, String> headers, String sheetName) {
//        if (CollectionUtils.isEmpty(list)) {
//            throw new IllegalArgumentException("数据源中没有任何数据!");
//        }
        if (StringUtils.isBlank(sheetName)) {
            throw new IllegalArgumentException("请输入sheet名称");
        }

        CellStyle cellDateStyle;
        CellStyle cellDataStyle;
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellDateStyle = workbook.createCellStyle();
        cellDateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        cellDataStyle = workbook.createCellStyle();
        cellDataStyle.setDataFormat(creationHelper.createDataFormat().getFormat("#,##0.00%"));

        int maxRow = getWorkbookMaxRow(workbook);
        int sheetNum = (int) Math.ceil(list.size() / (double) (maxRow - 1));
        Sheet[] sheets = new Sheet[sheetNum];
        // sheet名称
        if (sheetNum == 1) {
            sheets[0] = getSheet(workbook, sheetName);
        } else {
            for (int i = 0; i < sheets.length; i++) {
                sheets[i] = getSheet(workbook, sheetName + (i + 1));
            }
        }
        List<T> subList;
        for (int i = 0; i < sheets.length; i++) {
            if (i == sheets.length - 1) {
                subList = list.subList(i * (maxRow - 1), list.size());
            } else {
                subList = list.subList(i * (maxRow - 1), (i + 1) * (maxRow - 1));
            }
            fillSheetOld(sheets[i], subList, headers, cellDateStyle, cellDataStyle);
        }
    }

    /**
     * 多sheet
     * @param workbook
     * @param sheetNameToList
     * @param headers
     */
    public static <T> void listToMultiSheetExcel(Workbook workbook,Map<String,List<T>> sheetNameToList,List<ExcelHeader> headers) {
        sheetNameToList.forEach((sheetName,value)->{
            listToExcel(workbook, value, headers, sheetName, 0);
        });

        //对多sheet 进行排序
        List<String> sheetNameList = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            String sheetName = workbook.getSheetName(i);
            sheetNameList.add(sheetName);
        }
        List<String> orderedSheetNames = sheetNameList.stream().sorted().collect(Collectors.toList());
        int sheetSortFlag = 0;
        for (String sheetName : orderedSheetNames) {
            workbook.setSheetOrder(sheetName, sheetSortFlag);
            sheetSortFlag++;
        }

    }

    public static <T> void listToExcel(Workbook workbook, List<T> list, List<ExcelHeader> headers, String sheetName) {
        listToExcel(workbook, list, headers, sheetName, 0);
    }

    public static <T> void listToExcel(Workbook workbook, List<T> list, List<ExcelHeader> headers, String sheetName, int offset) {
        listToExcel(workbook, list, headers, sheetName, offset, 1);
    }

    public static <T> void listToExcel(Workbook workbook, List<T> list, List<ExcelHeader> headers, String sheetName, int offset, int headerOffset) {
        if (StringUtils.isBlank(sheetName)) {
            throw new IllegalArgumentException("请输入sheet名称");
        }
        int maxRow = getWorkbookMaxRow(workbook);

        // 处理单元格样式
        setHeaderStyle(workbook, headers);
        // 计算sheet数量
        int beginSheetNum, rowNum = 0;
        for (beginSheetNum = 0; true; beginSheetNum++) {
            int beginOffset = (beginSheetNum + 1) * headerOffset + offset;
            if (beginSheetNum * maxRow <= beginOffset && (beginSheetNum + 1) * maxRow > beginOffset) {
                // 找到所在页
                rowNum = beginOffset % maxRow;
                break;
            }
        }
        fillSheet(workbook, sheetName, list, headers, beginSheetNum, rowNum, headerOffset);
    }
    // endregion

    /**
     * @Description:  多表头导出 单sheet页 多header  此类型不适用行数超过maxRows
     * @Author: fei.wei
     * @Date:  2020/9/16 18:03
     **/
    public static <T> void listToMultiHeaderExcel(Workbook workbook, List<T> list, List<ExcelHeader> headers, String sheetName, int rowNum, int headerOffset) {
        if (StringUtils.isBlank(sheetName)) {
            throw new IllegalArgumentException("请输入sheet名称");
        }
        // 处理单元格样式
        setHeaderStyle(workbook, headers);
        // sheet名称
        sheetName = WorkbookUtil.createSafeSheetName(sheetName);
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            if (CollectionUtils.isNotEmpty(headers)) {
                setHeader(CellUtil.getRow(rowNum, sheet), headers);
                sheet.createFreezePane(0, 1, 0, 1);
                setWidth(sheet, headers);
            }
        }else{
            if (CollectionUtils.isNotEmpty(headers)) {
                setHeader(CellUtil.getRow(rowNum, sheet), headers);
                setWidth(sheet, headers);
            }
        }

        rowNum++;// 定义了表头，行加一
        // 定义存放字段名称的数组
        String[] fieldNames = new String[headers.size()];

        //填充数组
        int count = 0;
        for (ExcelHeader header : headers) {
            fieldNames[count] = header.getFieldName();
            count++;
        }

        //填充内容
        Row row;
        Cell cell;
        for (T item : list) {

            row = CellUtil.getRow(rowNum, sheet);
            for (int i = 0; i < fieldNames.length; i++) {
                Object objValue = getFieldValueByNameSequence(fieldNames[i], item);
                cell = CellUtil.getCell(row, i);
                if (headers.get(i).getCellStyle() != null) {
                    cell.setCellStyle(headers.get(i).getCellStyle());
                }
                // 如果是超链接
                if(headers.get(i) instanceof HyperlinkExcelHeader) {
                    if(objValue != null && StringUtils.isNotBlank(objValue.toString())) {
                        String[] split = objValue.toString().split(",|;");
                        cell.setHyperlink(getHyperlink(workbook, split[0]));
                        if(headers.get(i).getCellStyle() != null) {
                            cell.setCellStyle(headers.get(i).getCellStyle());
                        } else {
                            cell.setCellStyle(getLinkStyle(workbook));
                        }
                        cell.setCellValue(StringUtils.join(split, " "));
                    }
                } else if (objValue!=null && (objValue.toString().startsWith("http://") || objValue.toString().startsWith("https://"))) {
                    // 超链接
                    Hyperlink link = row.getSheet().getWorkbook().getCreationHelper().createHyperlink(HyperlinkType.URL);
                    link.setAddress(objValue.toString());
                    cell.setHyperlink(link);
                    cell.setCellStyle(getLinkStyle(workbook));
                    cell.setCellValue(objValue.toString());
                } else {
                    setCellValue(cell, objValue, headers.get(i));
                }
            }
            rowNum++;
        }
    }
    // region 辅助函数
    private static <T> void fillSheetOld(Sheet sheet, List<T> list, Map<String, String> headers, CellStyle cellDateStyle, CellStyle cellDataStyle) {
        // 设置标题
        setHeaderOld(CellUtil.getRow(0, sheet), headers);

        // 定义存放字段名称的数组
        String[] fieldNames = new String[headers.size()];

        //填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            fieldNames[count] = entry.getKey();
            count++;
        }

        //填充内容
        int rowNo = 1;
        Row row;
        Cell cell;
        for (T item : list) {
            row = CellUtil.getRow(rowNo, sheet);
            for (int i = 0; i < fieldNames.length; i++) {
                Object objValue = getFieldValueByNameSequence(fieldNames[i], item);
                cell = CellUtil.getCell(row, i);
                setCellValueOld(cell, objValue, cellDateStyle, cellDataStyle);
            }
            rowNo++;
        }
    }

    private static <T> void fillSheet(Workbook book, String sheetName, List<T> list, List<ExcelHeader> headers, int beginSheetNum, int rowNum, int headerOffset) {
        Sheet sheet = getSheet(book, beginSheetNum == 0 ? sheetName : (sheetName + beginSheetNum), headers);
        // 定义存放字段名称的数组
        String[] fieldNames = new String[headers.size()];

        int maxRow = getWorkbookMaxRow(book);
        //填充数组
        int count = 0;
        for (ExcelHeader header : headers) {
            fieldNames[count] = header.getFieldName();
            count++;
        }

        //填充内容
        Row row;
        Cell cell;
        for (T item : list) {
            if (rowNum > maxRow - 1) {
                beginSheetNum++;
                rowNum %= (maxRow - 1);
                rowNum += headerOffset - 1;
//                setWidth(sheet, headers);
                sheet = getSheet(book, beginSheetNum == 0 ? sheetName : (sheetName + beginSheetNum), headers);
            }
            row = CellUtil.getRow(rowNum, sheet);
            for (int i = 0; i < fieldNames.length; i++) {
                Object objValue = getFieldValueByNameSequence(fieldNames[i], item);
                cell = CellUtil.getCell(row, i);
                if (headers.get(i).getCellStyle() != null) {
                    cell.setCellStyle(headers.get(i).getCellStyle());
                }
                // 如果是超链接
                if(headers.get(i) instanceof HyperlinkExcelHeader) {
                    if(objValue != null && StringUtils.isNotBlank(objValue.toString())) {
                        String[] split = objValue.toString().split(",|;");
                        cell.setHyperlink(getHyperlink(book, split[0]));
                        if(headers.get(i).getCellStyle() != null) {
                            cell.setCellStyle(headers.get(i).getCellStyle());
                        } else {
                            cell.setCellStyle(getLinkStyle(book));
                        }
                        cell.setCellValue(StringUtils.join(split, " "));
                    }
                } else if (objValue!=null && (objValue.toString().startsWith("http://") || objValue.toString().startsWith("https://"))) {
                    // 超链接
//                    Hyperlink link = row.getSheet().getWorkbook().getCreationHelper().createHyperlink(HyperlinkType.URL);
//                    link.setAddress(objValue.toString());
                    cell.setHyperlink(getHyperlink(row.getSheet().getWorkbook(), objValue.toString()));
                    cell.setCellStyle(getLinkStyle(book));
                    cell.setCellValue(objValue.toString());
                } else {
                    setCellValue(cell, objValue, headers.get(i));
                }

            }
            rowNum++;
        }
//        setWidth(sheet, headers);
    }

    private static void setHeaderOld(Row row, Map<String, String> headers) {
        int i = 0;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            Cell cell = CellUtil.getCell(row, i);
            cell.setCellValue(entry.getValue());
            i++;
        }
    }

    private static void setHeader(Row row, List<ExcelHeader> headers) {
        int i = 0;
        for (ExcelHeader header : headers) {
            Cell cell = CellUtil.getCell(row, i);
            cell.setCellValue(header.getName());
            cell.setCellStyle(header.getHeaderCellStyle());
            i++;
        }
    }

    private static Sheet getSheet(Workbook book, String sheetName) {
        // sheet名称
        sheetName = WorkbookUtil.createSafeSheetName(sheetName);
        Sheet sheet = book.getSheet(sheetName);
        if (sheet == null) {
            sheet = book.createSheet(sheetName);
        }
        return sheet;
    }

    private static Sheet getSheet(Workbook book, String sheetName, List<ExcelHeader> headers) {
        // sheet名称
        sheetName = WorkbookUtil.createSafeSheetName(sheetName);
        Sheet sheet = book.getSheet(sheetName);
        if (sheet == null) {
            sheet = book.createSheet(sheetName);
            if (CollectionUtils.isNotEmpty(headers)) {
                setHeader(CellUtil.getRow(0, sheet), headers);
                // 同时冻结第一行 和 冻结第一列
                // sheet.createFreezePane( 1, 1, 1, 1 );
                // 只冻结第一行
                sheet.createFreezePane(0, 1, 0, 1);
                // 只冻结第一列
                // sheet.createFreezePane( 1, 0, 1, 0 )
                setWidth(sheet, headers);
            }
        }
        return sheet;
    }

    private static Object getCellValue(Cell cell) {
        Object value = null;
        if (cell == null) return value;
//        CellType cellType = cell.getCellType();
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = StringUtils.EMPTY;
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case ERROR:
                value = cell.getErrorCellValue();
                break;
            default:
                break;
        }
        return value;
    }

    private static void setCellValueOld(Cell cell, Object value, CellStyle cellDateStyle, CellStyle cellDataStyle) {
        if (value == null) return;
        Class clazz = value.getClass();
        if (clazz == String.class) {
            cell.setCellValue(value.toString());
        } else if (clazz == Integer.class || clazz == int.class) {
            cell.setCellValue(Integer.parseInt(value.toString()));
        } else if (clazz == Float.class || clazz == float.class) {
            if (cellDataStyle != null) {
                cell.setCellStyle(cellDataStyle);
            }
            cell.setCellValue(Float.parseFloat(value.toString()));
        } else if (clazz == Double.class || clazz == double.class) {
            if (cellDataStyle != null) {
                cell.setCellStyle(cellDataStyle);
            }
            cell.setCellValue(Double.parseDouble(value.toString()));
        } else if (clazz == Date.class) {
            if (cellDateStyle != null) {
                cell.setCellStyle(cellDateStyle);
            }
            cell.setCellValue((Date) value);
        } else if (clazz == Long.class || clazz == long.class) {
            cell.setCellValue(Long.parseLong(value.toString()));
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            cell.setCellValue(Boolean.parseBoolean(value.toString()));
        } else if (clazz == Byte.class || clazz == byte.class) {
            cell.setCellValue(Byte.parseByte(value.toString()));
        } else if (clazz == Character.class || clazz == char.class) {
            cell.setCellValue(value.toString());
        } else if (clazz == Short.class || clazz == short.class) {
            cell.setCellValue(Short.parseShort(value.toString()));
        } else if (value instanceof IBaseEnum) {
            cell.setCellValue(((IBaseEnum) value).getName());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private static void setCellValue(Cell cell, Object value, ExcelHeader header) {
        if (value == null) return;
        Class clazz = value.getClass();
        if (clazz == String.class) {
            cell.setCellValue(value.toString());
        } else if (clazz == Integer.class || clazz == int.class) {
            cell.setCellValue(Integer.parseInt(value.toString()));
        } else if (clazz == Float.class || clazz == float.class) {
            cell.setCellValue(Float.parseFloat(value.toString()));
        } else if (clazz == Double.class || clazz == double.class) {
//            if(ExcelHeader.class.isAssignableFrom(header.getClass())
//                    && header.getClass() != ExcelHeader.class) {
            cell.setCellValue(Double.parseDouble(value.toString()));
//            } else {
//                cell.setCellValue(value.toString());
//            }
        } else if (clazz == BigDecimal.class) {
//            if(ExcelHeader.class.isAssignableFrom(header.getClass())
//                    && header.getClass() != ExcelHeader.class) {
            cell.setCellValue(((Number) value).doubleValue());
//            } else {
//                cell.setCellValue(value.toString());
//            }
        } else if (clazz == Date.class) {
            cell.setCellValue((Date) value);
        } else if (clazz == Long.class || clazz == long.class) {
//            if(ExcelHeader.class.isAssignableFrom(header.getClass())
//            && header.getClass() != ExcelHeader.class) {
            cell.setCellValue(value.toString());
//            } else {
//                cell.setCellValue(value.toString());
//            }
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            cell.setCellValue(Boolean.parseBoolean(value.toString()));
        } else if (clazz == Byte.class || clazz == byte.class) {
            cell.setCellValue(Byte.parseByte(value.toString()));
        } else if (clazz == Character.class || clazz == char.class) {
            cell.setCellValue(value.toString());
        } else if (clazz == Short.class || clazz == short.class) {
            cell.setCellValue(Short.parseShort(value.toString()));
        } else if (value instanceof IBaseEnum) {
            cell.setCellValue(((IBaseEnum) value).getName());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     * @throws Exception
     * @MethodName : getFieldValueByNameSequence
     * @Description :
     * 根据带路径或不带路径的属性名获取属性值
     * 即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
     */
    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) {
        Object value = null;
        //将fieldNameSequence进行拆分
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            //根据属性名获取属性对象
            Object fieldObj = getFieldValueByName(attributes[0], o);
            String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf('.') + 1);
            value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;
    }

    /**
     * @param fieldName 字段名
     * @param o         对象
     * @return 字段值
     * @MethodName : getFieldValueByName
     * @Description : 根据字段名获取字段值
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        Object value = null;
        try {
            Field field = getFieldByName(fieldName, o.getClass());
            if (field != null) {
                field.setAccessible(true);
                value = field.get(o);
            } else {
                throw new NoSuchFieldException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
            }
        } catch (Exception ex) {
        }
        return value;
    }

    /**
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        //拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        //如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        //否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        //如果本类和父类都没有，则返回空
        return null;
    }

    /**
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param o          对象
     * @MethodName : setFieldValueByName
     * @Description : 根据字段名给对象的字段赋值
     */
    private static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            //获取字段类型
            Class<?> clazz = field.getType();
            String strValue = Objects.toString(fieldValue, StringUtils.EMPTY);
            if (fieldValue == null || StringUtils.isBlank(strValue)) {
                field.set(o, null);
                return;
            }
            //根据字段类型给字段赋值
            if (clazz == String.class) {
                if (fieldValue instanceof Number) {
                    if (((Number) fieldValue).doubleValue() == ((Number) fieldValue).intValue()) {
                        field.set(o, String.valueOf(((Number) fieldValue).intValue()));
                    } else {
                        field.set(o, String.valueOf(((Number) fieldValue).doubleValue()));
                    }
                } else {
                    field.set(o, strValue);
                }
            } else if (clazz == Integer.class || clazz == int.class) {
                field.set(o, fieldValue instanceof Number ? ((Number) fieldValue).intValue() : Integer.parseInt(strValue));
            } else if (clazz == Float.class || clazz == float.class) {
                field.set(o, fieldValue instanceof Number ? ((Number) fieldValue).floatValue() : Float.parseFloat(strValue));
            } else if (clazz == Double.class || clazz == double.class) {
                field.set(o, fieldValue instanceof Number ? ((Number) fieldValue).doubleValue() : Double.parseDouble(strValue));
            } else if (clazz == BigDecimal.class) {
                field.set(o, fieldValue instanceof Number ? new BigDecimal(((Number) fieldValue).doubleValue()) : ((BigDecimal) fieldValue).doubleValue());
            } else if (clazz == Date.class) {
                if (!StringUtils.isBlank(Objects.toString(fieldValue, StringUtils.EMPTY))) {
                    field.set(o, fieldValue);
                }
            } else if (clazz == Long.class || clazz == long.class) {
                field.set(o, fieldValue instanceof Number ? ((Number) fieldValue).longValue() : Long.parseLong(strValue));
            } else if (clazz == Boolean.class || clazz == boolean.class) {
                field.set(o, Boolean.parseBoolean(strValue));
            } else if (clazz == Byte.class || clazz == byte.class) {
                field.set(o, fieldValue instanceof Number ? ((Number) fieldValue).byteValue() : Byte.parseByte(strValue));
            } else if (clazz == Character.class || clazz == char.class) {
                field.set(o, strValue);
            } else if (clazz == Short.class || clazz == short.class) {
                field.set(o, fieldValue instanceof Number ? ((Number) fieldValue).shortValue() : Short.parseShort(strValue));
            } else if (IBaseEnum.class.isAssignableFrom(clazz)) {
                IBaseEnum[] enumConstants = (IBaseEnum[]) clazz.getEnumConstants();
                for (IBaseEnum item : enumConstants) {
                    if (item.getName().equals(strValue)) {
                        field.set(o, item);
                        return;
                    }
                }
                for (IBaseEnum item : enumConstants) {
                    if (item.getCode().equals(strValue)) {
                        field.set(o, item);
                        return;
                    }
                }
            } else {
                field.set(o, strValue);
            }
        } else {
            throw new RuntimeException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }
    }

    private static CellStyle getLinkStyle(Workbook workbook) {
        CellStyle linkStyle = workbook.createCellStyle();
        Font cellFont = workbook.createFont();
        cellFont.setUnderline((byte) 1);
        cellFont.setColor(IndexedColors.BLUE.index);
        linkStyle.setFont(cellFont);
        linkStyle.setWrapText(true);
        return linkStyle;
    }

    private static Hyperlink getHyperlink(Workbook workbook, String value) {
        Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
        if(StringUtils.isBlank(value)) return link;
        try {
            // 处理特殊字符
            value = value.replace(" ", "+");
            link.setAddress(value);
        } catch (Exception e) {
            log.error("设置超链接失败！", e);
        }
        return link;
    }

    public static void setHeaderStyle(Workbook workbook, List<ExcelHeader> headers) {
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle;
        for (ExcelHeader header : headers) {
            if (header.getCellStyle() == null && StringUtils.isNotBlank(header.getFormat())) {
                if("link".equalsIgnoreCase(header.getFormat())) {
                    Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                    header.setHyperlink(link);
                    header.setCellStyle(getLinkStyle(workbook));
                } else {
                    cellStyle = workbook.createCellStyle();
                    cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(header.getFormat()));
                    header.setCellStyle(cellStyle);
                }
            }
            if (header.getHeaderCellStyle() == null) {
                cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor((short) 22);
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                header.setHeaderCellStyle(cellStyle);
            }
        }
    }

    private static void setWidth(Sheet sheet, List<ExcelHeader> headers) {
        for (int i = 0; i < headers.size(); i++) {
            if(headers.get(i) instanceof DateTimeExcelHeader) {
                sheet.setColumnWidth(i, 4500);
                continue;
            }
            int colWidth = sheet.getColumnWidth(i) * 2;
            if (colWidth < 255 * 256) {
                sheet.setColumnWidth(i, colWidth < 4000 ? 4000 : colWidth);
            } else {
                sheet.setColumnWidth(i, 6000);
            }
//            sheet.autoSizeColumn(i);
//            int columnWidth = sheet.getColumnWidth(i);
//            sheet.setColumnWidth(i, columnWidth + 500);
            //设置单元格的宽度为最宽宽度+额外宽度
            //sheet.setColumnView(i, colWith + extraWith);
        }
    }

    private static int getWorkbookMaxRow(Workbook workbook) {
        if(workbook == null) return XLSMaxSheetRow;
        if(workbook instanceof HSSFWorkbook) {
            return XLSMaxSheetRow;
        } else if(workbook instanceof XSSFWorkbook) {
            return XLSXMaxSheetRow;
        } else if(workbook instanceof SXSSFWorkbook) {
            return XLSXMaxSheetRow;
        } else {
            return XLSMaxSheetRow;
        }
    }
    // endregion

    // region 其它方法

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings("unchecked")
    public static <T> void style(String title, List<String> headers, Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        Workbook workbook = new HSSFWorkbook();
        // 生成一个表格
        Sheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
//        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        CellStyle style2 = workbook.createCellStyle();
//        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        Font font2 = workbook.createFont();
//        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

//        // 声明一个画图的顶级管理器
//        Comment comment = sheet.getCellComment(1, 2);
//        // 设置注释内容
//        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
//        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//        comment.setAuthor("leno");

        // 产生表格标题行
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            RichTextString text = new HSSFRichTextString(headers.get(i));
            cell.setCellValue(text);
        }

        // 有图片时，设置行高为60px;
//        row.setHeightInPoints(60);
//        // 设置图片所在列宽度为80px,注意这里单位的一个换算
//        sheet.setColumnWidth(i, (short) (35.7 * 80));
//        // sheet.autoSizeColumn(i);
//        byte[] bsValue = (byte[]) value;
//        ClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
//                index);
//        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
//        //patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
    }
    // endregion
}