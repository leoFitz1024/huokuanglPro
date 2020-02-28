package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.User;
import com.tmqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by chenw on 2018/12/19.
 */
@Controller
@RequestMapping
public class LoginController {
    @Autowired
    private UserService userService;


    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value = "/loginApi", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult login(@RequestBody User user, HttpSession httpSession){
        System.out.print("编码是："+System.getProperty("file.encoding"));
        User loginUser = userService.findUserByUserName(user.getUsername());
        if(loginUser != null){
            if(loginUser.getPassword().equals(user.getPassword())){
                String userId = loginUser.getId().toString();
                httpSession.setAttribute("userId",userId);
                httpSession.setMaxInactiveInterval(86400);
                return new RestResultBuilder().setCode(1)
                        .setMsg("登陆成功")
                        .setData(loginUser.getUsername())
                        .build();//登陆成功
            }else{
                httpSession.setAttribute("userId","errLogin");
                return new RestResultBuilder().setCode(0)
                        .setMsg("密码错误")
                        .build();//密码错误
            }
        }else{
            return new RestResultBuilder().setCode(-1)
                    .setMsg("用户不存在")
                    .build();//用户不存在
        }

    }

    /**
     * 用户退出
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exitApi", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int exit(HttpSession httpSession){
        httpSession.removeAttribute("userId");
        return 1;//退出成功

    }

}
