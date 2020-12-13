package com.example.embedded;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

//socket

//Data
//Exception
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static boolean change = false;
    static String action="", item_name="";
    static Product item;
    private ListView product_list;
    private List<String> allItemName = new ArrayList<>(); //防止產品名重複
    private static List<Product> products = new ArrayList<>();
    private static List<Product> search_list = new ArrayList<>();
    private Context context = this;
    private SearchView searchView;
    private Dialog dialog;
    private ImageView imageView;
    private EditText editText;
    private Button check, cancel;
    final static String serverIP = "192.168.50.1";
    final static int port = 55688;
    boolean del = false;
    static Socket socket;
    static DataOutputStream out;
    static BufferedReader br;
    static boolean flag = true;
    Thread t;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = true;
        product_list = findViewById(R.id.product_item);
        Button create_btn = findViewById(R.id.increase_btn);
        searchView = findViewById(R.id.search_product);
        searchView.setIconified(false);

        products.add(new Product("A","KindA",1));
        products.add(new Product("B","KindB",2));
        products.add(new Product("C","KindC",3));
        products.add(new Product("D","AJDLVL",4));
        allItemName.add("KindA");
        allItemName.add("KindB");
        allItemName.add("KindC");
        allItemName.add("AJDLVL");

        changeList(products);

        searchView.setOnQueryTextListener(search_key);
        create_btn.setOnClickListener(increateProductListen);
        product_list.setOnItemLongClickListener(deleteEvent);
        Handler uiHandler = new Handler();

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                changeList(products);
            }
        });
        t = new Thread(runnable);
        t.start();

    }

    public void changeList(List<Product> product){
        productListAdapter productAdapter = new productListAdapter(this, R.layout.list_style, product);
        product_list.setAdapter(productAdapter);
    }

    private ListView.OnItemLongClickListener deleteEvent = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            item = products.get(position);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            if(item.getProduct_content() > 0){
                alertDialog.setTitle("無法刪除");
                alertDialog.setMessage(item.getProduct_name() + "還有"+ item.getProduct_content()+"在倉庫中");
                AlertDialog(alertDialog);
            }
            else{
                del = true;
                alertDialog.setTitle("刪除");
                alertDialog.setMessage("確定要刪除"+item.getProduct_name()+"？");
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        del = false;
                        dialog.dismiss();
                    }
                });
                AlertDialog(alertDialog);

            }
            return true;
        }
    };

    private SearchView.OnQueryTextListener search_key = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if(!search_list.isEmpty()){
                search_list.clear();
            }
            String key = searchView.getQuery().toString();
            for(int index=0; index<products.size();index++){
                if(products.get(index).getProduct_name().contains(key)){
                    search_list.add(products.get(index));
                }
            }
            if(search_list.isEmpty()){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("搜尋失敗");
                alertDialog.setMessage("未找到任何為" + key + "的產品");
                AlertDialog(alertDialog);
            }
            else{
                changeList(search_list);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            String key = searchView.getQuery().toString();
            if(key.isEmpty()){
                changeList(products);
                search_list.clear();
            }
            return false;
        }
    };

    private View.OnClickListener increateProductListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialoginit();
            dialog.show();
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tmp_name = editText.getText().toString();
                    if (allItemName.contains(tmp_name)){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("產品名稱重複");
                        alertDialog.setMessage("已經擁有"+tmp_name+"的產品");
                        AlertDialog(alertDialog);
                    }
                    else{
                        searchView.setQuery("",false);
                        Product product = new Product("A",tmp_name,0);
                        allItemName.add(tmp_name);
                        products.add(product);
                        changeList(products);
                        MainActivity.action = "add";
                        MainActivity.item = product;
                        dialog.dismiss();
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //拍照
                }
            });
        }
    };
    public void dialoginit(){
        dialog = new Dialog(this);
        dialog.setTitle("新增物品");
        dialog.setContentView(R.layout.increase_layout);
        imageView =  dialog.findViewById(R.id.create_img);
        editText = dialog.findViewById(R.id.create_name);
        check = dialog.findViewById(R.id.create_check);
        cancel = dialog.findViewById(R.id.create_cancel);
    }

    public void AlertDialog(AlertDialog.Builder alertDialog){
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(del){
                    System.out.println("del");
                    products.remove(item);
                    allItemName.remove(item.getProduct_name());
                    changeList(products);
                    MainActivity.action = "del";
                    del = false;
                }
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    Runnable runnable = new Runnable(){
//        Socket socket;
//        DataOutputStream out;
//        BufferedReader br;
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            while (flag) {
                try {
//                    InetAddress server = InetAddress.getByName("LAPTOP-FQI9PBM0");
                    socket = new Socket(serverIP, port);
                    out = new DataOutputStream(socket.getOutputStream());
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = "phone";
                    out.write(message.getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    //接收回傳
                    String return_str = br.readLine(); //需換行符號
                    System.out.println("catch: " + return_str);
                    long beginTime = System.currentTimeMillis();
                    while (socket.isConnected() && flag) {
                        switch (MainActivity.action) {
                            case "get":
                                if (item.getProduct_content() > 0) {
                                    message = "get/" + MainActivity.item_name;
                                    out.write(message.getBytes(StandardCharsets.UTF_8));
                                    out.flush();
                                    beginTime = System.currentTimeMillis();
                                    System.out.println(message);
                                    return_str = br.readLine(); //需換行符號
                                    if (return_str.equals("OK")) {
                                        products.get(products.indexOf(MainActivity.item)).setProduct_content(-1);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                changeList(products);
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                            alertDialog.setTitle("出貨失敗");
                                            alertDialog.setMessage("該產品沒有庫存");
                                            AlertDialog(alertDialog);
                                        }
                                    });
                                }
                                MainActivity.action = "";
                                MainActivity.item_name = "";
                                MainActivity.item = null;
                                break;
                            case "store":
                                message = "store/" + MainActivity.item_name;
                                out.write(message.getBytes(StandardCharsets.UTF_8));
                                out.flush();
                                beginTime = System.currentTimeMillis();
                                System.out.println(message);
                                return_str = br.readLine();
                                if (return_str.equals("OK")) {
                                    System.out.println("Store");
                                    products.get(products.indexOf(item)).setProduct_content(1);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            changeList(products);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                            alertDialog.setTitle("進貨失敗");
                                            alertDialog.setMessage("車上並未偵測到東西");
                                            AlertDialog(alertDialog);
                                        }
                                    });

                                }
                                MainActivity.action = "";
                                MainActivity.item_name = "";
                                MainActivity.item = null;
                                break;
                            case "add":
                                message = "add/" + MainActivity.item.getProduct_name() + "/" + MainActivity.item.getProduct_image();
                                out.write(message.getBytes(StandardCharsets.UTF_8));
                                out.flush();
                                beginTime = System.currentTimeMillis();
                                System.out.println(message);
                                MainActivity.action = "";
                                MainActivity.item = null;
                                break;
                            case "del":
                                message = "del/" + MainActivity.item.getProduct_name();
                                out.write(message.getBytes(StandardCharsets.UTF_8));
                                out.flush();
                                beginTime = System.currentTimeMillis();
                                System.out.println(message);
                                MainActivity.action = "";
                                MainActivity.item = null;
                                break;
                            case "init":
                                message = "init App";
                                out.write(message.getBytes(StandardCharsets.UTF_8));
                                out.flush();
                                beginTime = System.currentTimeMillis();
                                String[] tmp;
                                System.out.println(message);
                                do {
                                    return_str = br.readLine(); //需換行符號
                                    tmp = return_str.split(",");
                                    int number = Integer.parseInt(tmp[2]);
                                    products.add(new Product(tmp[0], tmp[1], number));
                                    allItemName.add(tmp[1]);
                                    System.out.println("catch: " + return_str);
                                } while (return_str.equals("finish"));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        changeList(products);
// 更新UI的操作
                                    }
                                });
                                break;
                            default:
                                long afterTime = System.currentTimeMillis();
                                if (afterTime - beginTime >= 5000) {
                                    message = "alive";
                                    out.write(message.getBytes(StandardCharsets.UTF_8));
                                    out.flush();
                                    beginTime = System.currentTimeMillis();
                                }
                                break;
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {

        try {
            flag = false;
            handler.removeCallbacks(runnable);
            t.join();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            out.close();
            br.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
