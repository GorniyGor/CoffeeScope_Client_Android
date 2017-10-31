package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.network.BaseResponse;

/**
 * Created by adm1n on 04.09.2017.
 */

public class AuthResponse extends BaseResponse {
    private String access_token;
    private String token_type;
    private String who;
    private Integer expires_in;

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
}
