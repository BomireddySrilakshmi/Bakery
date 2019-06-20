package com.vvitguntur.baker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vvitguntur.baker.Model.BakingApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycle_main)
    RecyclerView recyclerView;
    List<BakingApp> bakingAppList=new ArrayList<>();
    private static final String URL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(isConnectivity()) {
            StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    BakingApp[] recipe = gson.fromJson(response, BakingApp[].class);
                    bakingAppList.addAll(Arrays.asList(recipe));
                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this, bakingAppList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else
        {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this).setTitle("No Internet").
                    setMessage("Check Your Network Connectivity").setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }
    public Boolean isConnectivity()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        boolean isConnected=networkInfo!=null&&networkInfo.isConnected();
        return isConnected;
    }

}
