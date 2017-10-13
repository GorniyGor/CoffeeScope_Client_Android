package com.example.adm1n.coffeescope.network;

import com.example.adm1n.coffeescope.network.responses.AuthResponse;
import com.example.adm1n.coffeescope.network.responses.EditProfileResponse;
import com.example.adm1n.coffeescope.network.responses.ErrorResponse;
import com.example.adm1n.coffeescope.network.responses.ProfileResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by adm1n on 31.07.2017.
 */

public interface PrivateApiInterface {

    //Авторизация
    @FormUrlEncoded
    @POST("buyer/login")
    Single<AuthResponse> authorization(@Field("email") String email,
                                       @Field("password") String password);

    //Регистрация
    @FormUrlEncoded
    @POST("buyer/registration")
    Single<AuthResponse> registration(@Field("surname") String surName,
                                      @Field("name") String name,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("password_confirmation") String password_confirmation);

    @GET("buyer/profile")
    Single<ProfileResponse> getProfile();

    @FormUrlEncoded
    @POST("buyer/profile/edit")
    Single<EditProfileResponse> editProfile(
            @Field("name") String firstName,
            @Field("surname") String lastName,
            @Field("email") String email
    );

    //Refresh
    @GET("buyer/refresh")
    Call<AuthResponse> refresh();

    @FormUrlEncoded
    @POST("buyer/password/change")
    Single<ErrorResponse> changePassword(
            @Field("old_password") String oldPassword,
            @Field("password") String newPassword,
            @Field("password_confirmation") String newPasswordRepeat
    );
}
