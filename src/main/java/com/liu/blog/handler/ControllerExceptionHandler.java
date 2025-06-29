package com.liu.blog.handler;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 错误信息处理控制
 *
 * @author Strive
 * @version 1.0
 * @since 2025-06-29
 */
@SuppressWarnings("all")
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) {
        logger.error("Request URL : {},Exception : {}",request.getRequestURL(),e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url",request.getRequestURL());
        modelAndView.addObject("exception",e);
        modelAndView.setViewName("/error/error");
        return modelAndView;
    }

}
