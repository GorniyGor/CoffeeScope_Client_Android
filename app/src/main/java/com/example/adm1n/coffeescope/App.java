package com.example.adm1n.coffeescope;

import android.app.Application;
import android.content.Intent;

import com.example.adm1n.coffeescope.network.ApiInterface;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
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
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("myRealmFile")
                .build();

        Realm.setDefaultConfiguration(realmConfig);

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new okhttp3.Interceptor() {
//                    @Override
//                    public okhttp3.Response intercept(Chain chain) throws IOException {
//
//                        return null;
//                    }
//                })
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        Response response = chain.proceed(request);
//
//                        // todo deal with the issues the way you need to
//                        if (response.code() == 500) {
//                            startActivity(
//                                    new Intent(
//                                            ErrorHandlingActivity.this,
//                                            ServerIsBrokenActivity.class
//                                    )
//                            );
//
//                            return response;
//                        }
//
//                        return response;
//                    }
//                })
//                .build();

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://coffeescope.istomin.im/api/v1/")
//                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiInterface.class);
    }

    public static ApiInterface getApiInterface() {
        return api;
    }
}
