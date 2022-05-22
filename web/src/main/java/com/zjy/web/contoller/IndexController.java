package com.zjy.web.contoller;

import com.alibaba.fastjson.JSON;
import com.zjy.common.stratory.BaseActionParam;
import com.zjy.common.stratory.BaseActionResult;
import com.zjy.common.stratory.EventDispatcher;
import com.zjy.entity.model.DownloadTask;
import com.zjy.entity.model.TestDownloadRecord;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.DownlaodTaskService;
import com.zjy.service.UserService;
import com.zjy.service.stratory.close.CloseParam;
import com.zjy.service.stratory.create.CreateParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
@Slf4j
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DownlaodTaskService downlaodTaskService;

    @Autowired
    private EventDispatcher eventDispatcher;

    @GetMapping("/")
    public String index() {
        return "index";
    }













    @GetMapping("/index")
    @ResponseBody
    public String index2() {
        log.trace("/comm/getEnums");
        log.debug("/comm/getEnums");
        log.info("/comm/getEnums");
        log.warn("/comm/getEnums");
        log.error("it is error!");
        return "abc";
    }
    @GetMapping("/comm/getEnums")
    @ResponseBody
    public String getEnums() {
        int a = 1, b = 0;
        return "abc";
    }

    @GetMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("login");
        SavedRequest lastRequest = WebUtils.getSavedRequest(request);
        String lastUrl = null;
        if (lastRequest != null && HttpMethod.GET.toString().equalsIgnoreCase(lastRequest.getMethod())) {
            lastUrl = lastRequest.getRequestUrl();
        }
        if (StringUtils.isBlank(lastUrl)) {
            lastUrl = request.getRequestURL().toString().replace(request.getRequestURI(), StringUtils.EMPTY) + request.getContextPath() + "/";
        }
        mv.addObject("redirectUrl", lastUrl);
        return mv;
    }

    @GetMapping("/testPage")
    @RequiresPermissions({"myPer"})
    public String testPage() {
        UserInfo fromMaster = userService.getFromMaster();
        UserInfo fromSlave = userService.getFromSlave();

        return "testPage";
    }

    @GetMapping("/testMybatisPlus")
    @ResponseBody
    public String testMybatisPlus() {
        int i = userService.testMybatisPlus();
        return String.valueOf(i);
    }

    @PostMapping({"/login"})
    @ResponseBody
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zjy", "pass");
        subject.login(token);
        return "abc";
    }

    @GetMapping("/testDownload")
    @ResponseBody
    public String testDownload() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zjy", "pass");
        subject.login(token);
        return "abc";
    }

    @GetMapping("/testDownloadList")
    @ResponseBody
    public String testDownloadList() {
        DownloadTask task = new DownloadTask();
        task.setCreatedBy("1");
        task.setCreatedName("ad");
        downlaodTaskService.addTask(task);
        return "abc";
    }

    @GetMapping("/testCreateDownloadData")
    @ResponseBody
    public String testCreateDownloadData() {
        TestDownloadRecord record;
        for(int i = 0; i < 100000; i++) {
            log.info("插入第{}条数据", i);
            record = new TestDownloadRecord();
            record.setUserId(String.valueOf(i));
            record.setUserName("admin" + i);
            record.setUserCode("" + i);
            downlaodTaskService.addRecord(record);
        }
        return "abc";
    }

    @GetMapping("/testDisp")
    @ResponseBody
    public String testDisp() {
        BaseActionParam baseActionParam = new CreateParam();
        ((CreateParam)baseActionParam).setType("1");
        BaseActionResult baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));
        baseActionParam = new CloseParam();
        baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));


        baseActionResult = eventDispatcher.publishEvent(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));
        return "abc";
    }

    @RequestMapping("/hi")
    public String hello(Locale locale, Model model) {
        model.addAttribute("greeting", "Hello!");

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        model.addAttribute("currentTime", formattedDate);

        Map<String, Object> map = new HashMap<>();
        model.addAttribute("map", map);

        return "hello";
    }
}
