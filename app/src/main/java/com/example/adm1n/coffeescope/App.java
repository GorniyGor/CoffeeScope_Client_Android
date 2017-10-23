package com.example.adm1n.coffeescope;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.example.adm1n.coffeescope.network.BaseResponse;
import com.example.adm1n.coffeescope.network.PrivateApiInterface;
import com.example.adm1n.coffeescope.network.PublicApiInterface;
import com.example.adm1n.coffeescope.network.responses.AuthResponse;
import com.google.gson.Gson;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by adm1n on 31.07.2017.
 */

public class App extends Application {
    private static final int OK_CODE = 2;
    private static final int FAIL_CODE = 4;

    private Retrofit publicRetrofit;
    private Retrofit privateRetrofit;
    private Retrofit refreshRetrofit;
    private static PublicApiInterface publicApi;
    private static PrivateApiInterface privateApi;
    private static PrivateApiInterface refreshApi;
    private String token;
    private OkHttpClient httpClient;
    private String tokenType;

    private static Context context;

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
        context = getApplicationContext();

        Realm.init(this);
        final RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("myRealmFile")
                .build();

        Realm.setDefaultConfiguration(realmConfig);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham-medium.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        httpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        //Build new request
                        Request.Builder builder = request.newBuilder();
                        builder.header("Accept", "application/json"); //if necessary, say to consume JSON

                        String token = getToken(); //save token of this request for future
                        setAuthHeader(builder, token); //write current token to request

                        request = builder.build(); //overwrite old request
                        Response response = chain.proceed(request); //perform request, here original request will be executed

                        String responseBodyString = response.body().string();
                        Gson gson = new Gson();
                        BaseResponse baseResponse = gson.fromJson(responseBodyString, BaseResponse.class);
                        if (baseResponse.getStatus().equals(getString(R.string.error))) {
                            if (baseResponse.getFirstError().equals(getString(R.string.error_token_expired))) {
                                synchronized (httpClient) { //perform all 401 in sync blocks, to avoid multiply token updates
                                    String currentToken = getToken(); //get currently stored token
                                    if (currentToken != null && currentToken.equals(token)) { //compare current token with token that was stored before, if it was not updated - do update
                                        int code = refreshToken(); //refresh token
                                        if (code != OK_CODE) { //if refresh token failed for some reason
                                            if (code == FAIL_CODE) //only if response is 400, 500 might mean that token was not updated
//                                        logout(); //go to login screen
                                                return response.newBuilder()
                                                        .body(ResponseBody.create(response.body().contentType(), responseBodyString))
                                                        .build(); //if token refresh failed - show error to user
                                        }
                                    }

                                    if (getToken() != null) { //retry requires new auth token,
                                        setAuthHeader(builder, getToken()); //set auth token to updated
                                        request = builder.build();
                                        return chain.proceed(request); //repeat request with new token
                                    }
                                }
                            }
                        }
                        return response.newBuilder()
                                .body(ResponseBody.create(response.body().contentType(), responseBodyString))
                                .build();
                    }
                }).build();

        publicRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        refreshRetrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        privateRetrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();

        publicApi = publicRetrofit.create(PublicApiInterface.class);
        privateApi = privateRetrofit.create(PrivateApiInterface.class);
        refreshApi = refreshRetrofit.create(PrivateApiInterface.class);
    }

    public static PublicApiInterface getPublicApi() {
        return publicApi;
    }

    public static PrivateApiInterface getPrivateApi() {
        return privateApi;
    }

    public static PrivateApiInterface getRefreshApi() {
        return refreshApi;
    }

    private int refreshToken() {
        //// TODO: 12.10.2017 Обработать ошибку Инвалид Токен
        Call<AuthResponse> refresh = getRefreshApi().refresh();
        try {
            retrofit2.Response<AuthResponse> execute = refresh.execute();
            if (execute.isSuccessful()
                    && execute.body().getAccessToken() != null
                    && execute.body().getToken_type() != null) {
                saveToken(execute.body().getAccessToken(), execute.body().getToken_type());
                return OK_CODE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FAIL_CODE;
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

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
    }

    public static void logout() {
        privateApi.logout();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply();
    }
}
