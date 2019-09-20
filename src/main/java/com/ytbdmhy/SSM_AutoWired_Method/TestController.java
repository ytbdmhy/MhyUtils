package com.ytbdmhy.SSM_AutoWired_Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestDao testDao;

    @RequestMapping("/test")
    public String test() {
        // 第一种方法
//        new ThreadTest().run();

        // 第二种方法
        ThreadTest threadTest = new ThreadTest();
        threadTest.setTestDao(testDao);
        threadTest.run();


        return "over";
    }
}
