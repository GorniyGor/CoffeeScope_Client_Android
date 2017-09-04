package com.example.adm1n.coffeescope.network.responses;

import java.util.List;

/**
 * Created by adm1n on 04.09.2017.
 */

public class AuthResponse {
    public String status;
    public String accessToken;
    public String tokenType;
    public Integer expiresIn;
    public List<Object> error = null;

}
