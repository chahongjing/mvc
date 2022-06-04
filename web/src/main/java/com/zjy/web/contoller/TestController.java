package com.zjy.web.contoller;

import com.zjy.baseframework.enums.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

    @GetMapping("/index")
    public BaseResult<String> index() {
        return BaseResult.ok();
    }
}
