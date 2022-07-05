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
//-> 클래스 대신 오브젝트를 생성 처음부터 객체로 생긴것임
//    복제가안됨
//    setting basic retrofit
//    객체가 아니라서 class 가 아니므로 한번 연결하면 아무데서나 가져다 써도 같은 값임
    fun initRetrofit():Retrofit{

//    url안에 주소 적기 도메인도 가능 naver.com 등 : 포트번호 . addconvert->(gson) -> 제이슨을쓰겠다

    if (instance==null){
//        내로윙 -> 오브젝트 이므로 또 연결되려하면 안되므로 처음에만 인스턴스 생성 다음연결부터는 원래 instance 리턴
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



// 어떤 식으로 데이터를 주고 받을지 설정하는 interface ( cursor 의 역할 )
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


//    retrofit2 import
    @POST("/account")
    fun postAccount(
//    하나밖에 못해서 hash를 사용한
    @Body body:HashMap<String,String>
    ):Call<AccountData>


    @POST("/order")
    fun postOrder(
//    하나밖에 못해서 hash를 사용한
        @Body body:HashMap<String,Any>
    ):Call<InputHistory>
}