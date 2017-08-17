package com.example.adm1n.coffeescope;

import android.app.Application;

import com.example.adm1n.coffeescope.network.ApiInterface;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adm1n on 31.07.2017.
 */

public class App extends Application {
    private Retrofit retrofit;
    private static ApiInterface api;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://coffeescope.istomin.im/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiInterface.class);
    }

    public static ApiInterface getApiInterface () {
        return api;
    }
}
