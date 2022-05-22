package com.zjy.web.contoller;

import com.zjy.service.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comm")
public class CommonController {
    @Autowired
    private CommonService commonService;
    /**
     * 返回所有枚举对象
     *
     * @return
     */
    @RequestMapping(value = "/getEnums", produces = "application/javascript;charset=UTF-8")
    public String getEnums() {
        return commonService.getEnums();
    }
}
