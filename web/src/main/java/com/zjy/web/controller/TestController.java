package com.zjy.web.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zjy.baseframework.annotations.LimitByCount;
import com.zjy.baseframework.annotations.NoRepeatOp;
import com.zjy.baseframework.annotations.RedisCache;
import com.zjy.baseframework.common.DownloadException;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.baseframework.enums.FileSuffix;
import com.zjy.common.enums.HandleImportErrorType;
import com.zjy.common.enums.ImportExcelDataType;
import com.zjy.common.stratory.BaseActionParam;
import com.zjy.common.stratory.BaseActionResult;
import com.zjy.common.stratory.EventDispatcher;
import com.zjy.common.utils.BeanUtils;
import com.zjy.common.common.DateTimeExcelHeader;
import com.zjy.common.utils.DownloadUtils;
import com.zjy.common.common.ExcelHeader;
import com.zjy.common.utils.ExcelUtils;
import com.zjy.common.utils.FileUtils;
import com.zjy.common.common.HyperlinkExcelHeader;
import com.zjy.common.common.NumberExcelHeader;
import com.zjy.dao.vo.UserInfoVo;
import com.zjy.entity.enums.Sex;
import com.zjy.entity.model.DownloadTask;
import com.zjy.entity.model.TestDownloadRecord;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.DownlaodTaskService;
import com.zjy.service.UserService;
import com.zjy.service.service.UserInfoService;
import com.zjy.service.stratory.close.CloseParam;
import com.zjy.service.stratory.create.CreateParam;
import com.zjy.web.MyImportExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
    @Value("${mykey1}")
    private String myKey1;
    @Value("${mykey2.ab}")
    private String myKey2;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    @Autowired
    private DownlaodTaskService downlaodTaskService;

    @Autowired
    private EventDispatcher eventDispatcher;
    @Autowired
    MyImportExcel myImportExcel;

    @GetMapping("/index")
    public BaseResult<Map<String, Object>> index() {
        Map<String, Object> map = new HashMap<>();
        map.put("myKey1", myKey1);
        map.put("myKey2", myKey2);
        return BaseResult.ok(map);
    }

    /**
     * ??????????????????
     *
     * @param response
     * @return
     */
    @PostMapping("/startDownloadTask")
    public BaseResult<DownloadTask> startDownloadTask(Boolean download, HttpServletResponse response) {
        DownloadTask task = new DownloadTask();
        task.setCreatedBy(1L);
        task.setCreatedName("ad");
        downlaodTaskService.addTask(task, download, response);
        return download ? null : BaseResult.ok(task);
    }

    /**
     * ???????????????????????????
     *
     * @param id
     * @return
     */
    @GetMapping("/getDownloadTaskProgress")
    public BaseResult<DownloadTask> getDownloadTaskProgress(Long id) {
        return BaseResult.ok(downlaodTaskService.get(id));
    }

    /**
     * ??????????????????excel???????????????????????????DownloadTask??????????????????
     *
     * @param withError
     * @param response
     */
    @RequestMapping("/bigDataDownload")
    @LimitByCount(timeout = 3600)
    public void bigDataDownload(Boolean withError, HttpServletResponse response) {
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getUserId), "?????????"));
        headers.add(new DateTimeExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getCreatedOn), "????????????", "yyyy-MM-dd HH:mm:ss"));
        headers.add(new HyperlinkExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getLink), "??????????????????"));
        headers.add(new NumberExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getMoney), "??????", "??#,##0.00%"));
        headers.add(new NumberExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getNum), "??????????????????"));

        int pageSize = 500;
        Page<TestDownloadRecord> pager = new Page<>(1, pageSize);
        pager.setOrderBy("user_id");

        try {
            // ?????????????????????????????????
            PageInfo<TestDownloadRecord> pageInfo = downlaodTaskService.queryPageList(pager);
            if (pageInfo.getTotal() == 0) throw new DownloadException("????????????");

            long total = pageInfo.getTotal();
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                // ????????????
                if (withError != null && withError) {
                    int a = 1, b = 0;
                    int c = a / b;
                }

                // ??????SXSSFWorkbook??????????????????excel
                Workbook workbook = new SXSSFWorkbook();
                int pages = (int) Math.ceil(total * 1.0 / pageSize);
                for (int i = 0; i < pages; i++) {
                    if (i != 0) {
                        pager.setPageNum(i + 1);
                        pageInfo = downlaodTaskService.queryPageList(pager);
                        if (pageInfo.getTotal() == 0) {
                            break;
                        }
                    }
                    // ??????????????????????????????
                    ExcelUtils.listToExcel(workbook, pageInfo.getList(), headers, "sheet??????", i * pageSize, 1);
                    // ????????????????????? (int) (i * 100.0f / pages)
                    // todo: change
                    TimeUnit.SECONDS.sleep(1);
                }
                workbook.write(os);
                os.flush();
                byte[] bytes = os.toByteArray();
                try (ByteArrayInputStream swapStream = new ByteArrayInputStream(bytes)) {
                    DownloadUtils.download(swapStream, "??????excel." + FileSuffix.XLSX.getCode(), response);
                }
                // ?????????????????????/????????????
            }
        } catch (Exception e) {
            // ???????????????????????????
            throw new DownloadException("????????????", e);
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/testDispatcher")
    public String testDispatcher() {
        BaseActionParam baseActionParam = new CreateParam();
        ((CreateParam) baseActionParam).setType("1");
        BaseActionResult baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", jsonUtils.toJSON(baseActionResult));
        baseActionParam = new CloseParam();
        baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", jsonUtils.toJSON(baseActionResult));


        baseActionResult = eventDispatcher.publishEvent(baseActionParam);
        log.info("result:{}", jsonUtils.toJSON(baseActionResult));
        return "abc";
    }

    @GetMapping("/testCache")
    @RedisCache(key = "#{name}:#{id}", timeUnit = TimeUnit.SECONDS, expire = 10)
    public BaseResult<UserInfo> testCache(Long id, String name) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setName(name);
        userInfo.setBirthday(new Date());
        userInfo.setSex(Sex.FEMALE);
        return BaseResult.ok(userInfo);
    }

    @GetMapping("/testCache2")
    @RedisCache(key = "#{name}:#{id}", timeUnit = TimeUnit.SECONDS, expire = 10)
    public List<UserInfo> testCache2(Long id, String name) {
        List<UserInfo> list = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setName(name);
        userInfo.setBirthday(new Date());
        userInfo.setSex(Sex.FEMALE);
        list.add(userInfo);
        userInfo = new UserInfo();
        userInfo.setId(2L);
        userInfo.setName("dsfsd");
        list.add(userInfo);
        return list;
    }

    @GetMapping("/testCache3")
    @RedisCache(key = "#{name}:#{id}", timeUnit = TimeUnit.SECONDS, expire = 10)
    public UserInfo[] testCache3(Long id, String name) {
        UserInfo[] list = new UserInfo[2];
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setName(name);
        userInfo.setBirthday(new Date());
        userInfo.setSex(Sex.FEMALE);
        list[0] = (userInfo);
        userInfo = new UserInfo();
        userInfo.setId(2L);
        userInfo.setName("dsfsd");
        list[1] = (userInfo);
        return list;
    }

    @GetMapping("/testCache4")
    @RedisCache(key = "#{name}:#{id}", timeUnit = TimeUnit.SECONDS, expire = 10)
    public Map<Long, UserInfo> testCache4(Long id, String name) {
        Map<Long, UserInfo> list = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setName(name);
        userInfo.setBirthday(new Date());
        userInfo.setSex(Sex.FEMALE);
        list.put(1L, userInfo);
        userInfo = new UserInfo();
        userInfo.setId(2L);
        userInfo.setName("dsfsd");
        list.put(2L, userInfo);
        return list;
    }

    @GetMapping("/testCache5")
    @RedisCache(key = "#{userInfo.name}:#{userInfo.id}", timeUnit = TimeUnit.SECONDS, expire = 10)
    public Map<Long, UserInfo> testCache5(UserInfo userInfo) {
        Map<Long, UserInfo> list = new HashMap<>();
        list.put(userInfo.getId(), userInfo);
        return list;
    }

    @GetMapping("/testTransaction")
    @NoRepeatOp
    public BaseResult<Map<String, Object>> testTransaction() {
        userInfoService.insertTestTransaction();
        return BaseResult.ok();
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/testDataSource")
    @RequiresPermissions({"myPer"})
    public String testDataSource() {
        UserInfo fromRandom = userService.getFromRandom();
        UserInfo fromSlave = userService.getFromSlave();

        return "testPage";
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/testCreateDownloadData")
    public String testCreateDownloadData() {
        TestDownloadRecord record;
        for (int i = 0; i < 1000000; i++) {
            log.info("?????????{}?????????", i);
            record = new TestDownloadRecord();
            record.setUserName("admin" + i);
            record.setUserCode("" + i);
            record.setCreatedOn(new Date());
            record.setMoney((float) Math.random() * 100);
            record.setNum((long) (Math.random() * 100000));
            record.setLink("www.baidu.com");
            downlaodTaskService.addRecord(record);
        }
        return "abc";
    }

    @GetMapping("/r")
    @LimitByCount(count = 2, withParam = true)
    public String testR(Long id) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(Math.random());
    }

    @GetMapping("/t")
    public String testT() {
        FileUtils.a();
        return String.valueOf(Math.random());
    }

//    @GetMapping(value = "/favicon.ico")
//    public void favicon(HttpServletResponse response) {
//        OutputStream os = null;
//        try {
//            if(favicon_img == null) {
//                File file = ResourceUtils.getFile("classpath:static/favicon.ico");
//                favicon_img = FileUtils.readFileToByteArray(file);
//            }
//            response.setContentType("image/x-icon");
//            response.addHeader( "Cache-Control", "max-age=600" );
//            os = response.getOutputStream();
//            IOUtils.write(favicon_img, response.getOutputStream());
//        } catch (IOException e) {
//            log.error("??????????????????{}",e.getMessage());
//        } finally {
//            if (os != null) {
//                try {
//                    os.flush();
//                    os.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }

    @PostMapping("/testImportExcel")
    public BaseResult testImportExcel(MultipartFile myfile, HttpServletResponse response, Boolean downloadError, Boolean importCorrectData) throws IOException {
        if(downloadError == null) downloadError = false;
        if(importCorrectData == null) importCorrectData = false;
//        FileInputStream fileInputStream = new FileInputStream(new File("/home/zjy/a.xlsx"));
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("name", "??????"));
        headers.add(new ExcelHeader("sex", "??????"));
        myImportExcel.setHandleImportErrorType(downloadError ? HandleImportErrorType.DOWNLOAD_EXCEL_WITH_MESSAGE : HandleImportErrorType.RETURN_ERROR_MESSAGE);
        myImportExcel.setImportExcelDataType(importCorrectData ? ImportExcelDataType.IMPORT_CORRECT_DATA : ImportExcelDataType.INTERUPT_WHEN_ERROR);
        List<String> errorMsgList = myImportExcel.importExcel(myfile.getInputStream(), "Sheet1", UserInfoVo.class, headers, response);

        if(downloadError) {
            return null;
        } else {
            if(CollectionUtils.isNotEmpty(errorMsgList)) {
                return BaseResult.no("???????????????", errorMsgList);
            } else {
                return BaseResult.ok("???????????????");
            }
        }
    }

    @GetMapping("/testBatchInsert")
    public List<UserInfo> testBatchInsert() {
        List<UserInfo> userInfos = userInfoService.testBatchInsert();
        return userInfos;
    }
}
