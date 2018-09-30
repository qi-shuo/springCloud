package com.example.hello.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/30 上午11:13
 */
@RestController
public class TestController {

    @PostMapping("/test")
    public String test() {
        return "hello word";
    }
}
