package com.demo.medium.controller;

import com.demo.medium.service.MediumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/medium")
public class MediumController {

    @Resource
    private MediumService mediumService;

    @GetMapping("/data")
    public String getAllData() {
        mediumService.getData();
        return "ok";
    }

    @GetMapping("/article")
    public String getArticle() {
        mediumService.getArticle();
        return "ok";
    }
}
