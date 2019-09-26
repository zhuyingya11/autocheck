package com.gainetdb.autocheck.controller;

import com.gainetdb.autocheck.service.AttendMachineService;
import com.gainetdb.autocheck.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 移动考勤测试Controller
 */
@RestController
@RequestMapping("/check")
public class AttendMachineCheckController {

    @Autowired
    private AttendMachineService attendMachineService;


    //执行考勤
    @GetMapping("/doMachineSign/{jobnum}")
    public String create(@PathVariable String jobnum) {
        attendMachineService.attendMachineCheckByUrl(jobnum);
        return  "success";
    }


}
