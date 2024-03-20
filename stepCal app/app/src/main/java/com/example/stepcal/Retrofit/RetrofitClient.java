package com.example.stepcal.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;

<<<<<<< HEAD
    private static final String base_url="http://192.168.0.107:4006";



=======
    private static final String base_url="http://172.20.42.89:4006";
>>>>>>> c5a29c32086b3b73eace1f07fbf3305810204a7d

    public static Retrofit getRetrofit(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(base_url).
                    addConverterFactory(GsonConverterFactory.
                            create()).
                    build();
        }
        return retrofit;
    }
}
