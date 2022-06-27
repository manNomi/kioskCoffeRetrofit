package com.example.kiosckoutback.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosckoutback.R


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receipt_page_activity)
        var sequance=intent.getStringExtra("index")
        var total=intent.getStringExtra("total")
        for (index in 0 until sequance!!.toInt()) {
            var getCart=intent.getStringArrayExtra("cart${index}")
            cart.add(getCart!!)
        }
        initRecipt(total.toString())
        initBtn()
    }

    fun initRecipt(total:String){
        var text=""
        for (index in 0 until cart.size) {
            text+="${cart[index][0]} * ${cart[index][1]} \n"
        }
        findViewById<TextView>(R.id.reciptMenu).text = text
        findViewById<TextView>(R.id.reciptTotal).text="총합 : ${total}"
    }
    fun initBtn(){
        findViewById<TextView>(R.id.backBtn).setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}