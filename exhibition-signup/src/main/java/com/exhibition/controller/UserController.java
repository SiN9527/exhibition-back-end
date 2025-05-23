package com.exhibition.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open/api/user")
public class UserController {

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

}
