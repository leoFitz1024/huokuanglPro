package com.tmqt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by chenw on 2018/12/19.
 */
@SpringBootConfiguration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private ManageInterceptor manageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(manageInterceptor);
    }
}
