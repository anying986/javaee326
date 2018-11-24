package com.itheima.dao;

import com.itheima.domain.OrderItem;
import com.itheima.domain.OrderItemView;
import com.itheima.domain.Orders;
import com.itheima.utils.C3P0Utils;
import com.itheima.web.Constant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class OrderDao {
    private JdbcTemplate template=new JdbcTemplate(C3P0Utils.getDataSource());

    public void updateState(String r6_order) {
        String sql="update orders set state=? where oid=?";
        template.update(sql,Constant.ORDEDRS_YIFUKUAN,r6_order);
    }

    public void updateOrder(Orders order) {
        String sql="update orders set address=?,name=?,telephone=? where oid=?";
        template.update(sql,order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
    }

    public Orders orderById(String uid,String oid){
        String sql="select * from orders where uid=? and oid=?";
        Orders orders = template.queryForObject(sql, new BeanPropertyRowMapper<>(Orders.class), uid, oid);
        sql="select p.pid,p.pname,p.pimage,p.shop_price,o.count,o.subtotal from product p,orderitem o where p.pid=o.pid and o.oid=?";
        List<OrderItemView> viewList = template.query(sql, new BeanPropertyRowMapper<>(OrderItemView.class),oid);
        orders.setViewList(viewList);
        return orders;
    }

    public int getCount(String uid){
        String sql="select count(*) from orders where uid=?";
        return template.queryForObject(sql,int.class,uid);
    }

    public List<Orders> orderByPage(String uid,int currentPage,int pageSize){
        String sql="select * from orders where uid=? limit ?,?";
        List<Orders> ordersList = template.query(sql, new BeanPropertyRowMapper<>(Orders.class), uid, (currentPage - 1) * pageSize, pageSize);
        for (Orders orders : ordersList) {
            sql="select p.pid,p.pname,p.pimage,p.shop_price,o.count,o.subtotal from product p,orderitem o where p.pid=o.pid and o.oid=?";
            List<OrderItemView> viewList = template.query(sql, new BeanPropertyRowMapper<>(OrderItemView.class), orders.getOid());
            orders.setViewList(viewList);
        }

        return ordersList;
    }


    public void saveOrder(Orders orders){
        String sql="insert into orders values(?,?,?,?,?,?,?,?)";
        Object[] params={
               orders.getOid(),orders.getOrdertime(),orders.getTotal(),orders.getState(),
                orders.getAddress(),orders.getName(),orders.getTelephone(),orders.getUid()
        };
        template.update(sql,params);
    }

    public void saveOrderItem(OrderItem orderItem){
        String sql="insert into orderitem values(?,?,?,?)";
        template.update(sql,orderItem.getCount(),orderItem.getSubTotal(),orderItem.getPid(),orderItem.getOid());
    }



}
