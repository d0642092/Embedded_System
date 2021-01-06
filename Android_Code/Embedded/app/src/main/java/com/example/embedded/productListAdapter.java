package com.example.embedded;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import  java.lang.String;
import java.util.List;

public class productListAdapter extends ArrayAdapter<Product> {
    private AlertDialog.Builder alertDialog;
    public productListAdapter(@NonNull Context context, int resource, List<Product> list) {
        super(context, resource, list);
        alertDialog = new AlertDialog.Builder(context);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// 獲取老師的資料
        final Product item = getItem(position);
// 建立佈局
        View oneItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_style, parent, false);
// 獲取ImageView和TextView
        TextView product_name = (TextView) oneItemView.findViewById(R.id.product_name);
        TextView product_content = (TextView) oneItemView.findViewById(R.id.produvt_content);

        ImageView product_image = (ImageView) oneItemView.findViewById(R.id.product_image);
        if (item.getProduct_image() != null) {
            product_image.setBackground(null);
            Bitmap bitmap = MainActivity.photo.getPhoto(item.getProduct_image()+".jpg");
            if(bitmap != null){
                product_image.setImageBitmap(bitmap);
            } else {
                product_image.setImageResource(R.mipmap.img_test);
            }

        }
        else {
            product_image.setImageResource(R.mipmap.img_test);
        }
        Button store_BTN = (Button)oneItemView.findViewById(R.id.store_product);
        store_BTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                assert item != null;
                alertDialog.setTitle("進貨!");
                alertDialog.setMessage("將"+item.getProduct_name()+"放倉庫?");
                AlertDialog(alertDialog,"store",item);
            }
        });
        Button get_BTN = (Button)oneItemView.findViewById(R.id.get_product);
        get_BTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                assert item != null;
                alertDialog.setTitle("出貨!");
                alertDialog.setMessage("將"+item.getProduct_name()+"取出?");
                AlertDialog(alertDialog,"get",item);
            }
        });

        assert item != null;
        product_name.setText(item.getProduct_name());
        product_content.setText("數量" + item.getProduct_content());
        return oneItemView;
    }

    public void AlertDialog(AlertDialog.Builder alertDialog, final String action, final Product product){
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.item = product;
                MainActivity.item_name = product.getProduct_name();
                MainActivity.action = action;
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
