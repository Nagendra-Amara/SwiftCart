package com.example.swiftcart;

public class Product {

    private String pro;
    private int price;

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Product(String pro, int price) {
        this.pro = pro;
        this.price = price;
    }
}

