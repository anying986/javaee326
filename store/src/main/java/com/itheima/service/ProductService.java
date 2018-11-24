package com.itheima.service;

import com.itheima.dao.ProductDao;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    private ProductDao productDao=new ProductDao();

    public void addProduct(Product p){
        productDao.addProduct(p);
    }

    public PageBean<Product> findAll(int currentPage,int pageSize){
        PageBean<Product> pageBean=new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        int count = productDao.getAllCount();
        pageBean.setTotalCount(count);
        pageBean.setTotalPage((int) Math.ceil(count*1.0/pageSize));
        List<Product> list = productDao.findAll(currentPage, pageSize);
        pageBean.setList(list);
        return pageBean;
    }

    public Map<String,List<Product>> findNewHot(){
        Map<String,List<Product>> map=new HashMap<>();
        map.put("new",productDao.findNew());
        map.put("hot",productDao.findIsHot());
        return map;
    }

    public Product findById(String pid){
        return productDao.findById(pid);
    }

    public PageBean<Product> findByPage(String cid,int currentPage,int pageSize){
        PageBean<Product> pageBean=new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        int count = productDao.getCount(cid);
        pageBean.setTotalCount(count);
        pageBean.setTotalPage((int) Math.ceil(count*1.0/pageSize));
        List<Product> list = productDao.findByPage(cid, currentPage, pageSize);
        pageBean.setList(list);
        return pageBean;
    }
}
