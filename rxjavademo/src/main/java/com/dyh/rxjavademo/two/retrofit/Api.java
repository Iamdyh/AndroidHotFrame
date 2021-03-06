package com.dyh.rxjavademo.two.retrofit;


import com.dyh.rxjavademo.two.entity.LoginRequest;
import com.dyh.rxjavademo.two.entity.LoginResponse;
import com.dyh.rxjavademo.two.entity.RegisterRequest;
import com.dyh.rxjavademo.two.entity.RegisterResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface Api {
    @GET
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request);
}
