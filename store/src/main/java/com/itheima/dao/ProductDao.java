package com.itheima.dao;

import com.itheima.domain.Product;
import com.itheima.utils.C3P0Utils;
import com.itheima.web.Constant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProductDao {
    private JdbcTemplate template=new JdbcTemplate(C3P0Utils.getDataSource());

    public void addProduct(Product p) {
        String sql="insert into product values(?,?,?,?,?,?,?,?,?,?)";
        Object[] param={
                p.getPid(),p.getPname(),p.getMarket_price(),p.getShop_price(),
                p.getPimage(),p.getPdate(),p.getIs_hot(),p.getPdesc(),
                p.getPflag(),p.getCid()
        };
        template.update(sql,param);
    }

    public int getAllCount(){
        String sql="select count(pid) from product";
        return template.queryForObject(sql,int.class);
    }

    public List<Product> findAll(int currentPage,int pageSize){
        String sql="select * from product limit ?,?";
        return template.query(sql,new BeanPropertyRowMapper<>(Product.class),(currentPage-1)*pageSize,pageSize);
    }

    public int findByCid(String cid){
        String sql="select count(pid) from product where cid=?";
        return template.queryForObject(sql, int.class, cid);
    }

    public List<Product> findNew(){
        String sql="select * from product where pflag=? order by pdate desc limit 0,9";
        return template.query(sql,new BeanPropertyRowMapper<>(Product.class),Constant.PRODUCT_FLAG);
    }

    public List<Product> findIsHot(){
        String sql="select * from product where pflag=? and is_hot=? limit 0,9";
        return template.query(sql,new BeanPropertyRowMapper<>(Product.class),Constant.PRODUCT_FLAG,Constant.PRODUCT_ISHOT);
    }

    public Product findById(String pid){
        String sql = "select * from product where pid=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Product.class),pid);
    }

    public List<Product> findByPage(String cid,int currentPage,int pageSize){
        String sql="select * from product where cid=? limit ?,?";
        return template.query(sql,new BeanPropertyRowMapper<>(Product.class),cid,(currentPage-1)*pageSize,pageSize);
    }

    public int getCount(String cid){
        String sql="select count(*) from product where cid=?";
        return template.queryForObject(sql,int.class,cid);
    }

}
