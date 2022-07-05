package com.example.kiosckoutback.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosckoutback.*
import com.google.gson.GsonBuilder
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.HashMap


class ReceiptActivity : AppCompatActivity() {
    var cart= mutableListOf<Array<String>>()
    private var doubleBackToExit = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }

    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    lateinit var cartClass: CartClass

    var id =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receipt_page_activity)
        cartClass=intent.getSerializableExtra("cart")as CartClass

        id=intent.getStringExtra("id_value")!!
        initRecipt()
        initBtn()
        initRetrofit()
        recordHistory()
    }

    fun initRecipt(){
        var text=""
        for (index in 0 until  cartClass.cartCoffe.size){
            text+="${cartClass.cartCoffe[index].name} * ${cartClass.cartCoffe[index].count} \n"
        }

        findViewById<TextView>(R.id.reciptMenu).text = text
        findViewById<TextView>(R.id.reciptTotal).text="총합 : ${cartClass.totalCal()}"
    }
    fun initBtn(){
        findViewById<TextView>(R.id.backBtn).setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("id_value",id)
            startActivity(intent)
            finish()
        }
    }

    data class reciptData(
        val name:String,
        val count:Int,
        val sum_price:Int
    )
    val reciptList= mutableListOf<String>()

    fun recordHistory(){
        val gson= GsonBuilder()
            .setPrettyPrinting()
            .create()
        for (index in 0 until cartClass.cartCoffe.size) {
            val recipt=reciptData(cartClass.cartCoffe[index].name,cartClass.cartCoffe[index].count.toInt(),cartClass.cartCoffe[index].price.toInt() * cartClass.cartCoffe[index].count.toInt())
            val myAccountData=gson.toJson(recipt)
            reciptList.add(myAccountData)
        }



        var id=id
//        var orderList=""
//        orderList+="["
//        for (index in 0 until cartClass.cartCoffe.size) {
//            orderList += "{ 'name': ${cartClass.cartCoffe[index].name} ,'count:' ${cartClass.cartCoffe[index].count.toInt()},'sum_price': ${cartClass.cartCoffe[index].price.toInt() * cartClass.cartCoffe[index].count.toInt()} }"
//            if (index != cartClass.cartCoffe.size - 1) {
//                orderList += ","
//            }
//        }
//            orderList += "]"
//
//        Log.d("result",orderList)
        initEvent(id,reciptList,cartClass.totalCal())

    }

    lateinit var retrofit: Retrofit

    lateinit var retrofitHttp: RetrofitService

    fun initRetrofit() {
        retrofit= RetrofitClient.initRetrofit()
        retrofitHttp=retrofit!!.create(RetrofitService::class.java)

    }

    fun initEvent(id:String,data:MutableList<String>,total:Int){

        var requestData:HashMap<String,Any> = HashMap()
        requestData["id"]=id
        requestData["order_list"]=data
        requestData["total_price"]=total

        retrofitHttp.postOrder(requestData).
        enqueue(object: Callback<InputHistory> {
            //                통신 실패함수
            override fun onFailure(call: Call<InputHistory>, t: Throwable) {
                Log.d("result", "Request fail : ${t}")
//                    통신 연결과 실패했다면 실패한 이유도 나옴
            }
            //                  통신성공함수
            override fun onResponse(
                call: Call<InputHistory>,
                response: Response<InputHistory>
            ) {
                Log.d("result", "Request Success }")
                if (response.body()!!.success) {

                } else {
                    Log.d("result", response.body()!!.message)

                }
            }

        }
        )
    }


}