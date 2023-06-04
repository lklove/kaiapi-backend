package com.edkai.project.config.security;

import com.edkai.project.config.security.component.JwtAuthenticationTokenFilter;
import com.edkai.project.config.security.component.RestAccessDeniedHandler;
import com.edkai.project.config.security.component.RestAuthorizationEntryPoint;
import com.edkai.project.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String[] pathPatterns = {"/user/getCaptcha", "/user/register", "/user/login", "/user/loginBySms", "/v3/api-docs"};

    private String[] adminPath = {"/user/list/page",
            "/user/list",
            "/userInterfaceInfo/add",
            "/userInterfaceInfo/delete",
            "/userInterfaceInfo/update",
            "/userInterfaceInfo/get",
            "/userInterfaceInfo/list",
            "/userInterfaceInfo/list/page",
            "/interfaceInfo/list",
            "/interfaceInfo/list/AllPage",
            "/interfaceInfo/online",
            "/interfaceInfo/online",};

    @Autowired
    private RestAuthorizationEntryPoint authorizationEntryPoint;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;

//    @Autowired
//    private CustomFilter customFilter;

//    @Autowired
//    private CustomUrlDecisionManager customUrlDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //所需要用到的静态资源，允许访问
        web.ignoring().antMatchers("/swagger-ui.html",
                "/swagger-ui/*",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**",
                "/doc.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // 管理员才可访问的接口
                .antMatchers(adminPath).hasRole("admin")
                // 对于登录接口 允许匿名访问.anonymous()，即未登陆时可以访问，登陆后携带了token就不能再访问了
                .antMatchers(pathPatterns).anonymous()
                .antMatchers( "/user/checkUserLogin", "/user/getCaptcha", "/user/sendSms","/userInterfaceInfo/feign/add").permitAll()
                // 除上面外的所有请求全部需要鉴权认证,.authenticated()表示认证之后可以访问
                .anyRequest().authenticated()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setAccessDecisionManager(customUrlDecisionManager);
//                        object.setSecurityMetadataSource(customFilter);
//                        return object;
//                    }
//                })
                .and()
                .headers().cacheControl();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .authenticationEntryPoint(authorizationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        http.cors();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
}
