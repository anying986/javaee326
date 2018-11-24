package com.itheima.service;

import com.itheima.dao.CategoryDao;
import com.itheima.dao.ProductDao;
import com.itheima.domain.Category;
import com.itheima.exception.NoDelException;
import com.itheima.utils.JedisUtils;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryService {
    private CategoryDao categoryDao=new CategoryDao();
    private ProductDao productDao=new ProductDao();

    public void delCategory(String cid) throws NoDelException{
        int row = productDao.findByCid(cid);
        if(row>0){
            throw new NoDelException("分类下有商品,不可删除");
        }
        categoryDao.delCategory(cid);
        clearRedis();
    }

    public void updateCategory(String cid,String cname){
        categoryDao.updateCategory(cid,cname);
        clearRedis();
    }

    public Category findById(String cid){
        return categoryDao.findById(cid);
    }

    public void addCategory(String cname){
        categoryDao.addCategory(cname);
        clearRedis();
    }

    @Deprecated
    public List<Category> findAll(){
        Jedis jedis = null;
        try {
            jedis=JedisUtils.getJedis();
            String category = jedis.get("category");
            if(category==null){
                List<Category> list = categoryDao.findAll();
                jedis.set("category",JSONArray.fromObject(list).toString());
                return list;
            }else{
                JSONArray jsonArray = JSONArray.fromObject(category);
                List<Category> list = JSONArray.toList(jsonArray, Category.class);
                return list;
            }
        }catch (Exception e){
            return null;
        }finally {
            jedis.close();
        }

    }

    private void clearRedis(){
        Jedis jedis = JedisUtils.getJedis();
        jedis.del("category");
        jedis.close();
    }
}
