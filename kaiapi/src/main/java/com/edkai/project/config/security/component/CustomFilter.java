package com.edkai.project.config.security.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;



public class CustomFilter implements FilterInvocationSecurityMetadataSource {



    AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        String requestUrl = ((FilterInvocation) object).getRequestUrl();
//        List<Menu> menus = menuService.getMenusWithRole();
//        for (Menu menu : menus) {
//            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
//                String[] collect = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
//                return SecurityConfig.createList(collect);
//            }
//        }
//        return SecurityConfig.createList("ROLE_LOGIN");
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
