package com.itheima.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *  定义c3p0连接池工具类
 *  连接池: 标准接口 javax.sql.DataSource
 *  c3p0类  ComboPooledDataSource 实现接口DataSource
 */
public class C3P0Utils {
    //创建接口实现类,同时,自动读取配置文件 c3p0-config.xml
   private static DataSource dataSource = new ComboPooledDataSource();
   //提供静态方法,返回实现类对象
    public static DataSource getDataSource (){
        return  dataSource;
    }
    //提供静态方法,返回数据库连接对象
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
