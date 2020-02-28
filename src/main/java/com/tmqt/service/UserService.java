package com.tmqt.service;


import com.tmqt.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen wangkun on 2018/12/19.
 */
public interface UserService {
    int addUser(User user);

    int deleteUser(Integer id);

    int updateUser(User user);

    User findUserById(Integer id);

    User findUserByUserName(String username);

    List<User> findAllUser(Integer competence);

    List<User> findUserByIf(Map conditions);

    List<User> findPageUser(Integer competence,Integer pageNum, Integer pageSize);

    int countUser(Integer competence);
}
