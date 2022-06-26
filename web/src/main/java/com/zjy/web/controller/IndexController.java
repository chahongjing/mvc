package com.zjy.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController extends BaseController {
    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
