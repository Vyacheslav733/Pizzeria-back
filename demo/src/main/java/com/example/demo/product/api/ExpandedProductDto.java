package com.example.demo.product.api;

public class ExpandedProductDto extends ProductDto {
  private String typeName;
  private boolean isCartProduct;

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String name) {
    this.typeName = name;
  }

  public boolean getIsCartProduct() {
      return isCartProduct;
  }

  public void setIsCartProduct(boolean flag) {
      this.isCartProduct = flag;
  }
}
