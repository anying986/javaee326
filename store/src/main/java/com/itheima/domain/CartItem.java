package com.itheima.domain;

public class CartItem {

    private Product product;
    private int count;
    private double subTotal;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSubTotal() {
        return this.subTotal=count*product.getShop_price();
    }

//    public void setSubTotal(double subTotal) {
//        this.subTotal = subTotal;
//    }
}
