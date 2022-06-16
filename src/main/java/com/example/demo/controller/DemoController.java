package com.example.demo.controller;

import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping
    public String getDemo() {
        return "demo";
    }

    @PostMapping
    public String encodePcode(@RequestBody String pcode) throws Exception {
        return demoService.encodePcode(pcode);
    }
}
