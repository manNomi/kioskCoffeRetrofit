package com.example.kiosckoutback.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kiosckoutback.*
import com.example.kiosckoutback.Activity.LoginFromFragment
import com.example.kiosckoutback.Activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class JoinFragment: Fragment() {

    lateinit var retrofit: Retrofit
    lateinit var retrofitHttp: RetrofitService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val fragment=inflater.inflate(R.layout.join_page_fragment_join,container,false)
        initRetrofit()
        initEvent(fragment)
        return fragment
    }
    fun initEvent(fragment:View){
        val joinBtn = fragment.findViewById<TextView>(R.id.joinPage_btn)
        joinBtn.setOnClickListener {
            loginEvent(fragment)
        }
        val backBtn = fragment.findViewById<TextView>(R.id.back_btn)
        backBtn.setOnClickListener {
            Log.d("qwe","백버튼")
            val dataInterface = context as LoginFromFragment
            dataInterface.sendData(true,"to login")
        }
    }

    fun initRetrofit() {
        retrofit= RetrofitClient.initRetrofit()
        retrofitHttp=retrofit!!.create(RetrofitService::class.java)
    }

    fun loginEvent(fragment:View){
        retrofitHttp.getAccountLoginCheck(fragment.findViewById<EditText>(R.id.join_id_text).text.toString()).
        enqueue(object: Callback<AccountDataCheck> {
            override fun onFailure(call: Call<AccountDataCheck>, t: Throwable) {
            }
            override fun onResponse(call: Call<AccountDataCheck>, response: Response<AccountDataCheck>) {
                if (response.body()!!.success){
                    joinBtnEvent(fragment)
                }
                else{
                    Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun joinBtnEvent(fragment:View){
        var requestData:HashMap<String,String> = HashMap()
        requestData["id"]=fragment.findViewById<EditText>(R.id.join_id_text).text.toString()
        requestData["pw"]=fragment.findViewById<EditText>(R.id.join_pw_text).text.toString()
        requestData["name"]=fragment.findViewById<EditText>(R.id.join_name).text.toString()
        requestData["contact"]=fragment.findViewById<EditText>(R.id.join_contact).text.toString()
        retrofitHttp.postAccount(requestData).
        enqueue(object: Callback<AccountData> {
            override fun onFailure(call: Call<AccountData>, t: Throwable) {
                Log.d("result", "Request fail : ${t}")
            }
            override fun onResponse(
                call: Call<AccountData>,
                response: Response<AccountData>
            ) {
                Log.d("result", "Request Success }")
                if (response.body()!!.success) {
                    val dataInterface = context as LoginFromFragment
                    dataInterface.sendData(true,"to login")
                    Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("result", response.body()!!.message)
                    Toast.makeText(context, "${response.body()!!.message}회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}