package com.gainetdb.autocheck.test;



/**
 *
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScopeTestController {

    private int num = 0;

    @RequestMapping("/testScope")
    public int testScope() {
        System.out.println(++num);
        return num;
    }

    @RequestMapping("/testScope2")
    public int testScope2() {
        System.out.println(++num);
        return num;
    }

}