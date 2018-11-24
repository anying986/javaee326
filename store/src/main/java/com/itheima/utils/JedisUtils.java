package com.itheima.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/*
 * 数据库连接池的工具类
 * JedisPool返回Jedis对象
 *
 * ResourceBundle 类读取properties文件
 * 静态方法 static ResourceBundle getBundle("读取的文件名") 不要写后缀
 * ResourceBundle对象的方法,getString("文件中的键名")
 */
public class JedisUtils {
    //创建 JedisPool对象
    private  static JedisPool pool ;
    static {
        //读取配置文件中的数据
        //ResourceBundle类读取properties文件
        ResourceBundle re =  ResourceBundle.getBundle("config");
        //获取键值对
       int min = Integer.parseInt(re.getString("minIdle"));
       int max = Integer.parseInt(re.getString("maxIdle"));
       String localhost = re.getString("localhost");
       int port = Integer.parseInt(re.getString("port"));
       //创建连接池的配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(max);
        config.setMinIdle(min);
        // new连接池对象,传递配置对象,ip和端口
        pool = new JedisPool(config,localhost,port);

    }
    /**
     * 定义静态方法,返回Jedis对象
     */
    public  static Jedis getJedis(){
        return  pool.getResource();
    }
}
