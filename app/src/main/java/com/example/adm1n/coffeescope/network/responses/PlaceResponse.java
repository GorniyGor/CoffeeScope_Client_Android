package com.example.adm1n.coffeescope.network.responses;

import com.example.adm1n.coffeescope.models.Place;
import com.example.adm1n.coffeescope.network.BaseResponse;

/**
 * Created by adm1n on 01.08.2017.
 */

public class PlaceResponse extends BaseResponse {
    private Place data;

    public Place getData() {
        return data;
    }

    public void setData(Place data) {
        this.data = data;
    }
}
