package com.itheima.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<String,CartItem> map = new HashMap<>();
    private double total;

    public void  clearCart(){
        map.clear();
        total=0;
    }

    public void removeCart(String pid){
        CartItem item = map.remove(pid);
        total = total-item.getSubTotal();
    }

    public void addCart(CartItem cartItem){
        String pid = cartItem.getProduct().getPid();
        if (map.containsKey(pid)){
            CartItem original = map.get(pid);
            original.setCount(original.getCount()+cartItem.getCount());
        }else{
            map.put(pid,cartItem);
        }
        total=total+cartItem.getSubTotal();
    }

    public Collection<CartItem> getMap() {
        return map.values();
    }

    public void setMap(Map<String, CartItem> map) {
        this.map = map;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
