package com.liu.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * index控制
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-24
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }



}
