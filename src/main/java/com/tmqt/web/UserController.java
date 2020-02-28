package com.tmqt.web;

import com.tmqt.dto.RestResult;
import com.tmqt.dto.RestResultBuilder;
import com.tmqt.model.User;
import com.tmqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
@Controller
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 添加用户
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    /**
     * 删除用户
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int delUser(int id){
        return userService.deleteUser(id);
    }

    /**
     * 修改用户
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public int updateUser(@RequestParam Map user){
        User oldUser = userService.findUserById(Integer.parseInt(user.get("id").toString()));
        if(oldUser.getPassword().equals(user.get("oldPassWord"))){
            User newUser = new User();
            newUser.setId(Integer.parseInt(user.get("id").toString()));
            newUser.setPassword(user.get("newPassWord").toString());
            newUser.setRemarks(user.get("remarks").toString());
            return userService.updateUser(newUser);
        }else{
            return -1;
        }

    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult getUserList(@RequestParam("page") int pageNum, @RequestParam("limit") int pageSize, @RequestParam("competence") int competence){
        List<User> dataList = userService.findPageUser(competence,pageNum,pageSize);
        int count = userService.countUser(competence);
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setCount(count)
                .setData(dataList)
                .build();
    }

    /**
     * 筛选用户
     * @return
     */

    @RequestMapping(value = "/findUsers", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @Transactional
    public @ResponseBody
    RestResult findUsers(@RequestParam Map conditions){
        List<User> dataList = userService.findUserByIf(conditions);
        return new RestResultBuilder().setCode(0)
                .setMsg("")
                .setData(dataList)
                .build();
    }

}
