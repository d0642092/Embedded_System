package com.example.embedded;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
//socket

//Data
//Exception
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static String action="", item_name="";
    private ListView product_list;
    private static List<Product> products = new ArrayList<>();
    private static List<Product> search_list = new ArrayList<>();
    private productListAdapter productAdapter;
    private Context context = this;
    private SearchView searchView;
    final static String serverIP = "192.168.43.214";
    final static int port = 55688;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(runnable).start();

        products.add(new Product("A","KindA","number A"));
        products.add(new Product("B","KindB","number B"));
        products.add(new Product("C","KindC","number C"));
        products.add(new Product("D","AJDLVL","number P"));
        product_list = findViewById(R.id.product_item);
        productAdapter = new productListAdapter(this,R.layout.list_style, products);
        product_list.setAdapter(productAdapter);
        searchView = findViewById(R.id.search_product);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
                    productAdapter = new productListAdapter(context,R.layout.list_style, search_list);
                    product_list.setAdapter(productAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String key = searchView.getQuery().toString();
                if(key.isEmpty()){
                    System.out.println(key);
                    productAdapter = new productListAdapter(context,R.layout.list_style, products);
                    product_list.setAdapter(productAdapter);
                    search_list.clear();
                }
                return false;
            }
        });
    }
    public void AlertDialog(AlertDialog.Builder alertDialog){
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("yes");
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    Runnable runnable = new Runnable(){
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            try {
                Socket socket = new Socket(serverIP, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = "Phone";
                out.write(message.getBytes(StandardCharsets.UTF_8));
                out.flush();
                //接收回傳
                String return_str = br.readLine();
                System.out.println("catch: "+ return_str);
                System.out.println(MainActivity.action);
                while (socket.isConnected()) {
                    switch (MainActivity.action){
                        case "get":
                            System.out.println("get: " +MainActivity.action + " " + MainActivity.item_name);
                            message = "get " + MainActivity.item_name;
                            out.write(message.getBytes(StandardCharsets.UTF_8));
                            out.flush();
                            System.out.println(message);
                            MainActivity.action = "";
                            MainActivity.item_name = "";
                            break;
                        case "store":
                            message = "store " + MainActivity.item_name;
                            out.write(message.getBytes(StandardCharsets.UTF_8));
                            out.flush();
                            System.out.println(message);
                            MainActivity.action = "";
                            MainActivity.item_name = "";
//                            getMessage = input.readUTF();
//                            System.out.println(getMessage);
                            break;
                        default:
//                            System.out.println("Default: " + action + " " + item_name);
                            break;
                    }
//
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
