package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;

public class UserService {
    private UserDao userDao=new UserDao();

    public void addUser(User user){
        userDao.addUser(user);
    }

    public User login(String username,String password){
       return userDao.login(username,password);
    }
}
