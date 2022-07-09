package com.example.kiosckoutback.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosckoutback.Fragment.JoinFragment
import com.example.kiosckoutback.Fragment.LoginFragment
import com.example.kiosckoutback.R

//프라그먼트로 부터 데이터 받는 것 정의
interface LoginFromFragment{
    fun sendData(change:Boolean,move:String)
}

class LoginActivity: AppCompatActivity(),LoginFromFragment {
    private var doubleBackToExit = false

//    뒤로가기 재설정
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

//    시작함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        loginChange()
    }

//    조인 프라그먼트로 교체 함수
    fun joinChange(){
        val fragmentJoin = JoinFragment()
        val bundle = Bundle()
        fragmentJoin.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragmentJoin)
            .commit()
    }

//    로그인 프라그먼트로 교체 함수
    fun loginChange(){
        val fragmentLogin = LoginFragment()
        val bundle = Bundle()
        fragmentLogin.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragmentLogin)
            .commit()
    }

//    프라그먼트로 부터 데이터 받는 곳
    override fun sendData(change: Boolean, move: String) {
        if(change==true){
            if (move=="to join")
            joinChange()
            else{
                loginChange()
            }
        }
    }
}