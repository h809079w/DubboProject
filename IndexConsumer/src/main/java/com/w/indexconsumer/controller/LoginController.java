package com.w.indexconsumer.controller;



import com.w.common.domain.User;

import com.w.common.redis.RedisService;
import com.w.common.result.CodeMsg;
import com.w.common.result.Result;
import com.w.common.service.UserService;
import com.w.common.util.MD5Util;
import com.w.common.vo.LoginVo;
import com.w.indexconsumer.exception.BussinessException;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        String token = userService.login(response, loginVo);
        if (token.equals("登录失败")){
            throw new BussinessException(CodeMsg.SERVER_ERROR);
        }else if(token.equals("密码错误")){
            throw new BussinessException(CodeMsg.PASSWORD_ERROR);
        }
        return Result.success(token);
    }

    //面向vue的前端登录界面,由于vue使用json传参，这里采用@RequestBody注解参数
    @RequestMapping("/do_login2")
    @ResponseBody
    public Result<String> doLogin2(HttpServletResponse response, @Valid @RequestBody LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        String token = userService.login(response, loginVo);
        if (token.equals("登录失败")){
            throw new BussinessException(CodeMsg.SERVER_ERROR);
        }else if(token.equals("密码错误")){
            throw new BussinessException(CodeMsg.PASSWORD_ERROR);
        }
        return Result.success(token);
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
        String finalpassword = MD5Util.inputPassToDbPass(loginVo.getPassword(),MD5Util.saltDB);
        user.setPassword(finalpassword);
        user.setSalt(MD5Util.saltDB);
        boolean flag=userService.insertUserToDB(user);
        if(!flag){
            return Result.error(CodeMsg.RIGISTER_ERROR);
        }else{
            return Result.success("注册成功");
        }
    }
}
