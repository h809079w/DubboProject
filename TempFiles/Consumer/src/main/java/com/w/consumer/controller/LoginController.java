package com.w.consumer.controller;


import com.w.consumer.redis.RedisService;
import com.w.consumer.service.UserService;
import com.w.common.domain.User;
import com.w.common.vo.LoginVo;

import com.w.consumer.result.CodeMsg;
import com.w.consumer.result.Result;

import com.w.consumer.util.ShiroMD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(@Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        String username=loginVo.getId().toString();
        String password=loginVo.getPassword();
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.PASSWORD_ERROR);

        }

        return Result.success(token.toString());
    }

    @RequestMapping("/to_register")
    public String toRegister(){
        return "register";
    }
    @RequestMapping("/do_register")
    @ResponseBody
    public Result<String> doRegister(@Valid LoginVo loginVo){
        User user=new User();
        user.setId(loginVo.getId());
        String finalpassword = ShiroMD5Util.getFinalPassword(loginVo.getId().toString(),loginVo.getPassword());
        user.setPassword(finalpassword);
        boolean flag=userService.insertUserToDB(user);
        if(!flag){
            return Result.error(CodeMsg.RIGISTER_ERROR);
        }else{
            return Result.success("注册成功");
        }
    }
}
