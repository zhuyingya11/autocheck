package com.gainetdb.autocheck.controller;

import com.gainetdb.autocheck.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 移动考勤测试Controller
 */
@RestController
@RequestMapping("/mobilecheck")
public class CheckController {

    @Autowired
    private CheckService checkService;


    //执行考勤
    @GetMapping("/doSign")
    public String create() {
        checkService.check();
        return  "success";
    }


}
