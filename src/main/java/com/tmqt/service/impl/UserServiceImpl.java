package com.tmqt.service.impl;

import com.github.pagehelper.PageHelper;
import com.tmqt.dao.UserMapper;
import com.tmqt.model.User;
import com.tmqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        Date date = new Date();
        user.setAddtime(date);
        return userMapper.insertSelective(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User findUserByUserName(String username) {
        return userMapper.selectByUserName(username);
    }

    @Override
    public List<User> findAllUser(Integer competence) {
        return userMapper.selectAll(competence);
    }

    @Override
    public List<User> findUserByIf(Map conditions) {
        return userMapper.selectByIf(conditions);
    }

    @Override
    public List<User> findPageUser(Integer competence,Integer pageNum, Integer pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectAll(competence);
    }

    @Override
    public int countUser(Integer competence) {
        return userMapper.countUser(competence);
    }
}
