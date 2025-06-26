package com.liu.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * index控制
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@Controller
@RequestMapping
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }


}
