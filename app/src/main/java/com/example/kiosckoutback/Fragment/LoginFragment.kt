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
import com.example.kiosckoutback.AccountLoginData
import com.example.kiosckoutback.Activity.LoginActivity
import com.example.kiosckoutback.Activity.LoginFromFragment
import com.example.kiosckoutback.Activity.MainActivity
import com.example.kiosckoutback.R
import com.example.kiosckoutback.RetrofitClient
import com.example.kiosckoutback.RetrofitService
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val fragment=inflater.inflate(R.layout.login_page_fragment_login,container,false)

        initRetrofit()

        initEvent(fragment)

        return fragment
    }

    fun initEvent(fragment:View){
        //            로그인버튼
        val loginBtn = fragment.findViewById<TextView>(R.id.login_btn)
        loginBtn.setOnClickListener {
            Log.d("qwe","로그인버튼")
            loginEvent(fragment)

        }
        val joinBtn = fragment.findViewById<TextView>(R.id.join_btn)
        joinBtn.setOnClickListener {
            val dataInterface = context as LoginFromFragment
            dataInterface.sendData(true,"to join")
        }
    }
    lateinit var retrofit: Retrofit

    lateinit var retrofitHttp: RetrofitService


    fun initRetrofit() {
        retrofit = RetrofitClient.initRetrofit()
        retrofitHttp = retrofit!!.create(RetrofitService::class.java)
    }

    var idValue=""
    var pwValue=""


    fun loginEvent(fragment:View){
        idValue=fragment.findViewById<EditText>(R.id.id_text)!!.text.toString()
        pwValue=fragment.findViewById<TextInputEditText>(R.id.pw_text)!!.text.toString()

        retrofitHttp.getAccountLogin(idValue,pwValue).
        enqueue(object: Callback<AccountLoginData> {
            //                통신 실패함수
            override fun onFailure(call: Call<AccountLoginData>, t: Throwable) {
                Log.d("result","Request fail : ${t}")
//                    통신 연결과 실패했다면 실패한 이유도 나옴
            }
            //                  통신성공함수
            override fun onResponse(call: Call<AccountLoginData>, response: Response<AccountLoginData>) {
                Log.d("result","Request Success }")
                if (response.body()!!.success){
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("id_value",idValue)
                    startActivity(intent)
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()

                }
                else{
                    Log.d("result",response.body()!!.message)
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()

                }
            }
        }
    )

    }


}