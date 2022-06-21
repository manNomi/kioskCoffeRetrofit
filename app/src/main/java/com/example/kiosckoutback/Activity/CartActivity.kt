package com.example.kiosckoutback.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosckoutback.Fragment.PastaFragment
import com.example.kiosckoutback.Fragment.SteakFragment
import com.example.kiosckoutback.Fragment.WineFragment
import com.example.kiosckoutback.R
import org.w3c.dom.Text

class CartActivity : AppCompatActivity() {

    private var doubleBackToExit = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L){
                doubleBackToExit = false
            }
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    var cart= mutableListOf<Array<String>>()
    var totalPay=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_page_activity)

        var sequance=intent.getStringExtra("index")
        for (index in 0 until sequance!!.toInt()) {
        var getCart=intent.getStringArrayExtra("cart${index}")
            cart.add(getCart!!)
        }

//        for (index in 0 until cart.size){
//            Log.d("qwe123","${cart[index][2]}원 ${cart[index][1]}개")
//        }

        totalCal()
        initCart(cart)
        initEvent()
    }
    fun totalCal(){
        totalPay=0
        val total = findViewById<TextView>(R.id.totalCart2)
        for (index in 0 until cart.size){
            totalPay+=cart[index][2].toInt()*cart[index][1].toInt()
        }
        total.text="총합 : ${totalPay.toString()}"
    }

    fun initEvent() {
        val back_btn = findViewById<Button>(R.id.backBtn)
        back_btn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("index", cart.size.toString())
            var text= mutableListOf<String>()
            for(index in 0 until cart.size) {
                text.add("cart" + "${index}")
                intent.putExtra(text[index], cart[index])
            }
            startActivity(intent)
            finish()
        }

        val payBtn=findViewById<Button>(R.id.payBtn)
        payBtn.setOnClickListener{
            if (totalPay==0)
            {
                Toast.makeText(this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("index", cart.size.toString())
                intent.putExtra("total",totalPay.toString())

                var text= mutableListOf<String>()
                for(index in 0 until cart.size) {
                    text.add("cart" + "${index}")
                    intent.putExtra(text[index], cart[index])
                }
                startActivity(intent)
                finish()
            }
        }

    }

    fun initCart(list:MutableList<Array<String>>){
        val linearLayout = findViewById<LinearLayout>(R.id.customCart)
        val viewArray= mutableListOf<TextView>()
        for (index in 0 until list.size){
        val customLinear = layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
        customLinear.findViewById<TextView>(R.id.cartName).text = list[index][0]
        customLinear.findViewById<TextView>(R.id.cartValue).text=list[index][1]
            val view=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArray.add(view)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArray)}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArray,linearLayout,customLinear)}
        linearLayout.addView(customLinear)
    }
    }

    fun psEvent(number: Int,view: MutableList<TextView>) {
        cart[number][1]=(cart[number][1].toInt()+1).toString()
        view[number].text=cart[number][1]
        totalCal()
    }

    fun msEvent(number: Int,view: MutableList<TextView>,linearLayout: LinearLayout,customView: View) {
        if(cart[number][1].toInt()==1)
        {
            cart.removeAt(number)
            linearLayout.removeView(customView)
        }
        else {
            cart[number][1] = (cart[number][1].toInt() - 1).toString()
            view[number].text = cart[number][1]
        }
        totalCal()
    }


}