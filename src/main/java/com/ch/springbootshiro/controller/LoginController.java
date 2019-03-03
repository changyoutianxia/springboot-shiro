package com.ch.springbootshiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("/toLogin")
    @ResponseBody
    public String login(){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
        usernamePasswordToken.setUsername("zhangsan");
        usernamePasswordToken.setPassword("123456".toCharArray());
        try {
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "fail";
        }
        return "login success";
    }
    @RequestMapping("/hasPermission")
    @ResponseBody
    @RequiresPermissions(value = {"user:add"})
    public String hasPermission(){
        return "has permission";
    }

    @RequestMapping("/hasRole")
    @ResponseBody
    @RequiresRoles(value = {"admin"})
    public String hasRole(){
        return "use role";
    }
}
