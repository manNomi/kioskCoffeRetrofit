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



interface LoginFromFragment{
    fun sendData(change:Boolean,move:String)
}

class LoginActivity: AppCompatActivity(),LoginFromFragment {

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        loginChange()
    }

    fun joinChange(){
        val fragmentJoin = JoinFragment()
        val bundle = Bundle()
        fragmentJoin.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragmentJoin)
            .commit()
    }

    fun loginChange(){
        val fragmentLogin = LoginFragment()
        val bundle = Bundle()
        fragmentLogin.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragmentLogin)
            .commit()
    }

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