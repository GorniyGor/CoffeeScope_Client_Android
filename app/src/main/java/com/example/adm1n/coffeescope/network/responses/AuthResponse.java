package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.network.BaseResponse;

import java.util.List;

/**
 * Created by adm1n on 04.09.2017.
 */

public class AuthResponse extends BaseResponse {
    private String first_error;
    private String access_token;
    private String token_type;
    private String who;
    private Integer expires_in;
    private List<Errors> error = null;
//    private Errors errors;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getFirst_error() {
        return first_error;
    }

    public void setFirst_error(String first_error) {
        this.first_error = first_error;
    }

    public List<Errors> getError() {
        return error;
    }

    public void setError(List<Errors> error) {
        this.error = error;
    }
}
