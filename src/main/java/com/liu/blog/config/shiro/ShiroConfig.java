package com.liu.blog.config.shiro;


import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Autowired UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联UserRealm
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Autowired DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 关联安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        HashMap<String,String> map = new LinkedHashMap<>();
        map.put("/admin/login","anon");
        map.put("/admin/**","auth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        // 设置拦截器后的登陆页面（自定义）
        shiroFilterFactoryBean.setLoginUrl("/admin");

        return shiroFilterFactoryBean;
    }


}
