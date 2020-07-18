package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.customer.Customer;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.LoginSerivce;
import com.mysb.core.service.UserService;
import com.mysb.core.utils.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    @Reference
    private UserService userService;

    //@Autowired
    // private PasswordEncoder passwordEncoder;
    @Reference
    private LoginSerivce loginSerivce;


    @RequestMapping("/register")
    public Result register(@RequestBody Customer customer) {
        try {
//            //获取明文密码
//            String password = customer.getPassword();
//            //对明文密码进行加密
//            String securityPassword = passwordEncoder.encode(password);
//            //把加密后的密码存储到seller对象中
//            customer.setPassword(securityPassword);
            userService.register(customer);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "用户名已被注册");
        }
    }

    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        try {
            if (phone == "" || "".equals(phone)) {
                return new Result(false, "手机号不能为空");
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                return new Result(false, "手机格式不正确");
            }
            userService.sendCode(phone);
            return new Result(true, "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发送失败");
        }
    }

    @RequestMapping("/findByUsername")
    public Customer findByUsername(String username){
        return userService.findById(username);
    }

    @RequestMapping("/saveCustomer")
    public Result saveCustomer(@RequestBody Customer customer){

        try {
            System.out.println(customer);
            userService.saveCustomer(customer);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }


}
