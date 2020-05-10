package com.roman.excel.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller("/ca")
public class HelloController {

    @GetMapping("/foo")
    @ResponseBody
    public Object foo(){
        return "success";
    }

    public static void main(String[] args) {
        int count = 100;
        int mode = 3;
        System.out.println(100 % 3);
        System.out.println(100 / 3);
    }
}
