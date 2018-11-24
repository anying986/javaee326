package com.itheima.dao;

import com.itheima.domain.User;
import com.itheima.utils.C3P0Utils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;



public class UserDao {
    private JdbcTemplate template=new JdbcTemplate(C3P0Utils.getDataSource());

    public void addUser(User user){
        String sql="insert into user values(?,?,?,?,?,?,?,?,?,?)";
        Object[] param={
            user.getUid(),user.getUsername(),user.getPassword(),
            user.getName(),user.getEmail(),user.getBirthday(),user.getGender(),
                user.getState(),user.getCode(),user.getRemark()
        };
        template.update(sql,param);
    }

    public User login(String username,String password){
        String sql="select * from user where username=? and password=?";
        try {
            return template.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),username,password);
        }catch (Exception e) {
            return null;
        }
    }
}
