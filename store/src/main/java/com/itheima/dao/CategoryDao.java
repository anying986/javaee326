package com.itheima.dao;

import com.itheima.domain.Category;
import com.itheima.utils.C3P0Utils;
import com.itheima.utils.UUIDUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;

public class CategoryDao {
    private JdbcTemplate template=new JdbcTemplate(C3P0Utils.getDataSource());

    public void delCategory(String cid){
        String sql="delete from category where cid=?";
        template.update(sql,cid);
    }

    public void updateCategory(String cid,String cname){
        String sql="update category set cname=? where cid=?";
        template.update(sql,cname,cid);
    }

    public Category findById(String cid){
        String sql="select * from category where cid=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Category.class),cid);
    }

    public void addCategory(String cname){
        String sql="insert into category values(?,?)";
        template.update(sql,UUIDUtils.getUUID(),cname);
    }

    public List<Category> findAll(){
        String sql="select * from category";
        return template.query(sql,new BeanPropertyRowMapper<>(Category.class));
    }
}
