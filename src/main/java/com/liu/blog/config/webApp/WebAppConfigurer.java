package com.liu.blog.config.webApp;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    /**
     * 上传地址
     */
    @Value("${file.upload.path}")
    private String filePath;

    /**
     * 上传相对地址
     */
    @Value("${file.upload.relativePath}")
    private String fileRelativePath;

    /**
     * 添加资源控制
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileRelativePath)
                .addResourceLocations("file:" + filePath + "/");
    }

}
