package com.example.adm1n.coffeescope;

import android.app.Application;
import android.preference.PreferenceManager;

import com.example.adm1n.coffeescope.network.BaseResponse;
import com.example.adm1n.coffeescope.network.PrivateApiInterface;
import com.example.adm1n.coffeescope.network.PublicApiInterface;
import com.example.adm1n.coffeescope.network.responses.AuthResponse;
import com.example.adm1n.coffeescope.network.responses.ErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adm1n on 31.07.2017.
 */

public class App extends Application {
    private Retrofit publicRetrofit;
    private Retrofit privateRetrofit;
    private static PublicApiInterface publicApi;
    private static PrivateApiInterface privateApi;
    private OkHttpClient client;
    private String token;
    private String tokenType;

    public String getToken() {
        return PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString(Const.API_TOKEN, " ");
    }

    public String getTokenType() {
        return PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString(Const.API_TOKEN_TYPE, " ");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        final RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("myRealmFile")
                .build();

        Realm.setDefaultConfiguration(realmConfig);

        //C AUTH
        client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain
                                .proceed(chain.request())
                                .newBuilder()
                                .header("Accept", "application/json")
                                .header("Authorization", getTokenType() + getToken())
                                .build();
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        String responseBodyString = response.body().string();
                        Gson gson = new Gson();
                        BaseResponse baseResponse = gson.fromJson(responseBodyString, BaseResponse.class);
                        if (baseResponse.getStatus().equals(getString(R.string.error))) {
                            ErrorResponse message = gson.fromJson(responseBodyString, ErrorResponse.class);
                            if (message.getFirstError().equals(getString(R.string.error_token_expired))
                                    && message.getStatus().equals(getString(R.string.expired))) {
                                refreshToken();
                            }
                        } else if (baseResponse.getStatus().equals(getString(R.string.success))) {
                            AuthResponse responseOk = gson.fromJson(responseBodyString, AuthResponse.class);
                            saveToken(responseOk.getAccessToken(), responseOk.getToken_type());
                        }
                        return response.newBuilder()
                                .body(ResponseBody.create(response.body().contentType(), responseBodyString)).build();
                    }
                })
                .build();

        publicRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        privateRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getString(R.string.base_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        publicApi = publicRetrofit.create(PublicApiInterface.class);
        privateApi = privateRetrofit.create(PrivateApiInterface.class);
    }

    public static PublicApiInterface getPublicApi() {
        return publicApi;
    }

    public static PrivateApiInterface getPrivateApi() {
        return privateApi;
    }

    private void refreshToken() {
        privateApi.refresh()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthResponse>() {
                    @Override
                    public void accept(@NonNull AuthResponse authResponse) throws Exception {
                        //todo выполнить запрос, который был до этого
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //возможна реккурсия
                    }
                });
    }

    private void saveToken(String token, String type) {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit()
                .putString(Const.API_TOKEN, token)
                .apply();

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit()
                .putString(Const.API_TOKEN_TYPE, type)
                .apply();
    }
}
