package com.vx.dyvide.controller.restAPI.Michelin.service;

import com.vx.dyvide.model.HERE.PriceResponse;
import com.vx.dyvide.model.Michelin.Iti;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MichelinService {
    @GET("1/route.json/eng/header")
    Call<String> getHeader(@Query(value="steps", encoded=true) String steps, @Query("fuelConsump.city") String consumption, @Query("fuelCost") float fuelCost, @Query("cy") String currency, @Query("authkey") String apiKey, @Query("multipleIti") boolean multipleIti, @Query("callback") String callbackFunction);

    @GET("1/route.json/eng/roadsheet")
    Call<String> getRoadsheet(@Query(value="steps", encoded=true) String steps, @Query("fuelConsump.city") String consumption,@Query("fuelCost") float fuelCost, @Query("cy") String currency, @Query("authkey") String apiKey, @Query("callback") String callbackFunction);


}
