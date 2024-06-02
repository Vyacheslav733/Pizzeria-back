package com.example.demo.orders.api;

public class ExpandedOrderProductDto extends OrderProductDto {
    private String productName;
    private Double productPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double price) {
        this.productPrice = price;
    }
}
