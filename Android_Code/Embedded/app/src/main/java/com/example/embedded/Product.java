package com.example.embedded;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Product {
    private static int productID=1;
    private String product_image;
    private String product_name, product_content;

    public Product(String product_image, String product_name, String product_content){
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_content = product_content;
        productID++;
    }

    public String getProduct_name(){
        return this.product_name;
    }
    public String getProduct_content(){
        return this.product_content;
    }
    public String getProduct_image(){
        return this.product_image;
    }
}
