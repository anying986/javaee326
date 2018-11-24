package com.itheima.service;

import com.itheima.dao.OrderDao;
import com.itheima.domain.OrderItem;
import com.itheima.domain.Orders;
import com.itheima.domain.PageBean;
import com.itheima.utils.C3P0Utils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderDao orderDao=new OrderDao();

    public Orders orderById(String uid,String oid){
        return orderDao.orderById(uid,oid);
    }

    public PageBean<Orders> orderByPage(String uid,int currentPage,int pageSize){
        PageBean<Orders> pageBean=new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        int count = orderDao.getCount(uid);
        pageBean.setTotalCount(count);
        pageBean.setTotalPage((int) Math.ceil(count*1.0/pageSize));
        List<Orders> list = orderDao.orderByPage(uid, currentPage, pageSize);
        pageBean.setList(list);
        return pageBean;
    }

    public void submitOrder(Orders orders, List<OrderItem> list){
        Connection con=null;
        try {
            TransactionSynchronizationManager.initSynchronization();
            con = DataSourceUtils.getConnection(C3P0Utils.getDataSource());
            con.setAutoCommit(false);
            orderDao.saveOrder(orders);
            for (OrderItem orderItem : list) {
                orderDao.saveOrderItem(orderItem);
            }
            con.commit();
        }catch (Exception e){
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateOrder(Orders order) {
        orderDao.updateOrder(order);
    }

    public void updateState(String r6_order) {
        orderDao.updateState(r6_order);
    }
}
