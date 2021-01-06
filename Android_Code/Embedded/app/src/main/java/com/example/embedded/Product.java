package com.example.embedded;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Product {
    private String product_image;
    private String product_name;
    private int product_content;

    public Product(String product_image, String product_name, int product_content){
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_content = product_content;
    }
    public Product(){
        this.product_image = null;
        this.product_name = null;
        this.product_content = 0;
    }
    public String getProduct_name(){
        return this.product_name;
    }
    public int getProduct_content(){
        return this.product_content;
    }
    public String getProduct_image(){
        return this.product_image;
    }
    public void setProduct_content(int i){
        this.product_content += i;
    }
    public void setProduct_image(String i){
        this.product_image = i;
    }
    public void setProduct_name(String i){
        this.product_name = i;
    }
}
