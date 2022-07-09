package com.example.kiosckoutback

import android.view.Menu
import com.google.gson.JsonArray
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*
import kotlin.collections.HashMap

object RetrofitClient {
    var instance:Retrofit?=null
    fun initRetrofit():Retrofit{
    if (instance==null){
        instance=Retrofit.Builder()
            .baseUrl("http://3.39.66.6:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
        return instance!!
    }
}

data class AccountLoginData(
    var message:String ,
    var success:Boolean
    )

data class AccountData(
    var message:String ,
    var success:Boolean
)

data class AccountDataCheck(
    var message:String ,
    var success:Boolean
)

data class CategoryData(
    var message: String,
    var success: Boolean,
    var data: JsonArray
)

data class MenuData(
    var message: String,
    var success: Boolean,
    var data: JsonArray
)

data class InputHistory(
    var message:String ,
    var success:Boolean
)

data class GetHistroy(
    var message: String,
    var success: Boolean,
    var data: JsonArray
)

interface RetrofitService{
    @GET("/account/login")
    fun getAccountLogin(
        @Query("id") id:String,
        @Query("pw") pw:String
    ):Call<AccountLoginData>

    @GET("/account/overlap")
    fun getAccountLoginCheck(
        @Query("id") id:String
    ):Call<AccountDataCheck>


    @GET("/category")
    fun getAccountCategory(
        @Query("lang") lang:String
    ):Call<CategoryData>

    @GET("/category/menu")
    fun getAccountMenu(
        @Query("lang") lang:String,
        @Query("category_name") category_name:String
    ):Call<MenuData>

    @GET("/order")
    fun getHistory(
        @Query("id") id:String,
    ):Call<GetHistroy>

    @POST("/account")
    fun postAccount(
    @Body body:HashMap<String,String>
    ):Call<AccountData>


    @POST("/order")
    fun postOrder(
        @Body body:HashMap<String,Any>
    ):Call<InputHistory>
}