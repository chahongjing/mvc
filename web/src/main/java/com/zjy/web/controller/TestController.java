package com.zjy.web.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zjy.baseframework.annotations.RedisCache;
import com.zjy.baseframework.common.DownloadException;
import com.zjy.baseframework.common.RedisKeyUtils;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.baseframework.enums.FileSuffix;
import com.zjy.common.stratory.BaseActionParam;
import com.zjy.common.stratory.BaseActionResult;
import com.zjy.common.stratory.EventDispatcher;
import com.zjy.common.utils.BeanUtils;
import com.zjy.common.utils.DateTimeExcelHeader;
import com.zjy.common.utils.DownloadUtils;
import com.zjy.common.utils.ExcelHeader;
import com.zjy.common.utils.ExcelUtils;
import com.zjy.common.utils.HyperlinkExcelHeader;
import com.zjy.common.utils.NumberExcelHeader;
import com.zjy.entity.model.DownloadTask;
import com.zjy.entity.model.TestDownloadRecord;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.DownlaodTaskService;
import com.zjy.service.UserService;
import com.zjy.service.service.UserInfoService;
import com.zjy.service.stratory.close.CloseParam;
import com.zjy.service.stratory.create.CreateParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @GetMapping("/index")
    public BaseResult<Map<String, Object>> index() {
        Map<String, Object> map = new HashMap<>();
        map.put("myKey1", myKey1);
        map.put("myKey2", myKey2);
        return BaseResult.ok(map);
    }

    /**
     * 开启下载任务
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
     * 获取下载任务的进度
     *
     * @param id
     * @return
     */
    @GetMapping("/getDownloadTaskProgress")
    public BaseResult<DownloadTask> getDownloadTaskProgress(Long id) {
        return BaseResult.ok(downlaodTaskService.get(id));
    }

    /**
     * 大数据量生成excel，并下载，如果抛出DownloadTask前端报错提示
     *
     * @param withError
     * @param response
     */
    @RequestMapping("/bigDataDownload")
    public void bigDataDownload(Boolean withError, HttpServletResponse response) {
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getUserId), "工单号"));
        headers.add(new DateTimeExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getCreatedOn), "创建时间", "yyyy-MM-dd HH:mm:ss"));
        headers.add(new HyperlinkExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getLink), "日志文件路径"));
        headers.add(new NumberExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getMoney), "门店", "¥#,##0.00%"));
        headers.add(new NumberExcelHeader(BeanUtils.convertToFieldName(TestDownloadRecord::getNum), "门店接单时长"));

        int pageSize = 500;
        Page<TestDownloadRecord> pager = new Page<>(1, pageSize);
        pager.setOrderBy("user_id");

        try {
//            String key = RedisKeyUtils.getDownloadCertificateAssignKey();
//            Object downThreadNum = stringRedisTemplate.opsForValue().get(key);
//            if(downThreadNum != null && ((int)downThreadNum) > 5) {
//                // 同时下载人数超过5人，提示下载人数过多
//                throw new DownloadException("下载人数过多");
//            }
//            stringRedisTemplate.opsForValue().increment(key);
//            // 如果有人下载，则key续约，如果没有人下载，1个小时后自动删除，防止线上服务崩溃没有decrement导致服务不可用
//            stringRedisTemplate.expire(key, 1, TimeUnit.HOURS);
            // 数据库记录开始下载任务
            PageInfo<TestDownloadRecord> pageInfo = downlaodTaskService.queryPageList(pager);
            if (pageInfo.getTotal() == 0) throw new DownloadException("没有数据");

            long total = pageInfo.getTotal();
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                // 模拟报错
                if (withError != null && withError) {
                    int a = 1, b = 0;
                    int c = a / b;
                }

                // 使用SXSSFWorkbook处理大数据量excel
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
                    // 获取到分页数据后导出
                    ExcelUtils.listToExcel(workbook, pageInfo.getList(), headers, "sheet名称", i * pageSize, 1);
                    // 数据库更新进度 (int) (i * 100.0f / pages)
                    // todo: change
                    TimeUnit.SECONDS.sleep(1);
                }
                workbook.write(os);
                os.flush();
                byte[] bytes = os.toByteArray();
                try (ByteArrayInputStream swapStream = new ByteArrayInputStream(bytes)) {
                    DownloadUtils.download(swapStream, "测试excel." + FileSuffix.XLSX.getCode(), response);
                }
                // 数据库记录下载/上传完成
            }
        } catch (Exception e) {
            // 数据库记录下载失败
            throw new DownloadException("下载出错", e);
        } finally {
//            stringRedisTemplate.opsForValue().decrement(key);
        }
    }

    /**
     * 测试模板模式
     *
     * @return
     */
    @GetMapping("/testDispatcher")
    public String testDispatcher() {
        BaseActionParam baseActionParam = new CreateParam();
        ((CreateParam) baseActionParam).setType("1");
        BaseActionResult baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));
        baseActionParam = new CloseParam();
        baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));


        baseActionResult = eventDispatcher.publishEvent(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));
        return "abc";
    }

    @GetMapping("/testCache")
    @RedisCache(key = RedisKeyUtils.KEY_PREFIX + "#{name}:#{id}", timeUnit = TimeUnit.SECONDS, expire = 10)
    public String testCache(Long id, String name) {
        return new Date().toString();
    }

    @GetMapping("/testTransaction")
    public BaseResult<Map<String, Object>> testTransaction() {
        userInfoService.insertTestTransaction();
        return BaseResult.ok();
    }

    /**
     * 测试动态数据源
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
     * 模拟生成大量数据
     *
     * @return
     */
    @GetMapping("/testCreateDownloadData")
    public String testCreateDownloadData() {
        TestDownloadRecord record;
        for (int i = 0; i < 1000000; i++) {
            log.info("插入第{}条数据", i);
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
//            log.error("获取图片异常{}",e.getMessage());
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
}