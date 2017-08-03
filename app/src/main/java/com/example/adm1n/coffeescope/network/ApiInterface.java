package com.example.adm1n.coffeescope.network;

import com.example.adm1n.coffeescope.network.responses.PlaceResponse;
import com.example.adm1n.coffeescope.network.responses.PlacesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by adm1n on 31.07.2017.
 */

public interface ApiInterface {

    @GET("places")
    Observable<PlacesResponse> getPlaces();

    @GET("place/{placeId}")
    Observable<PlaceResponse> getPlace(@Path("placeId") String placeId);
}
