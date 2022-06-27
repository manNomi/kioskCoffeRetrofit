package com.example.kiosckoutback.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosckoutback.CartClass
import com.example.kiosckoutback.R

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

    lateinit var cartClass: CartClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_page_activity)

        var sequance=intent.getStringExtra("index")

        cartClass=intent.getSerializableExtra("DATA") as CartClass
//        for (index in 0 until sequance!!.toInt()) {
//        var getCart=intent.getStringArrayExtra("cart${index}")
//            cart.add(getCart!!)
//        }
        totalCal()
        initCart()
        initEvent()
    }

    fun totalCal(){
        val total = findViewById<TextView>(R.id.totalCart2)
        total.text="총합 : ${cartClass.totalCal()}"
    }

    fun initEvent() {
        val back_btn = findViewById<Button>(R.id.backBtn)
        back_btn.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)


//            intent.putExtra("index", cart.size.toString())
//            var text= mutableListOf<String>()
//            for(index in 0 until cart.size) {
//                text.add("cart" + "${index}")
//                intent.putExtra(text[index], cart[index])
//            }
            intent.putExtra("index", "true")
            intent.putExtra("cart",cartClass)

            startActivity(intent)
            finish()
        }
        val payBtn=findViewById<Button>(R.id.payBtn)
        payBtn.setOnClickListener{
            if (cartClass.totalCal()==0)
            {
                Toast.makeText(this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("total",cartClass.totalCal().toString())
//                var text= mutableListOf<String>()
//                for(index in 0 until cart.size) {
//                    text.add("cart" + "${index}")
//                    intent.putExtra(text[index], cart[index])
//                }
//
                intent.putExtra("cart",cartClass)
                startActivity(intent)
                finish()
            }
        }
    }

    fun initCart(){
        val linearLayout = findViewById<LinearLayout>(R.id.customCart)
        val viewArraySteak= mutableListOf<TextView>()
        for (index in 0 until cartClass.cartSteak.size){
        val customLinear = layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
        customLinear.findViewById<TextView>(R.id.cartName).text = cartClass.cartSteak[index][0]
        customLinear.findViewById<TextView>(R.id.cartValue).text=cartClass.cartSteak[index][1]
            val viewSteak=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArraySteak.add(viewSteak)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArraySteak,"steak")}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArraySteak,linearLayout,customLinear,"steak")}
        linearLayout.addView(customLinear)
        }

        val viewArrayPasta= mutableListOf<TextView>()

        for (index in 0 until cartClass.cartPasta.size){
            val customLinear = layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.cartName).text = cartClass.cartPasta[index][0]
            customLinear.findViewById<TextView>(R.id.cartValue).text=cartClass.cartPasta[index][1]
            val viewPasta=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArrayPasta.add(viewPasta)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArrayPasta,"pasta")}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArrayPasta,linearLayout,customLinear,"pasta")}
            linearLayout.addView(customLinear)
        }

        val viewArrayWine= mutableListOf<TextView>()

        for (index in 0 until cartClass.cartWine.size){
            val customLinear = layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.cartName).text = cartClass.cartWine[index][0]
            customLinear.findViewById<TextView>(R.id.cartValue).text=cartClass.cartWine[index][1]
            val viewWine=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArrayWine.add(viewWine)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArrayWine,"wine")}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArrayWine,linearLayout,customLinear,"wine")}
            linearLayout.addView(customLinear)
        }
    }

    fun psEvent(number: Int,view: MutableList<TextView>,type:String) {
        if(type=="steak"){
            cartClass.cartSteak[number][1]=(cartClass.cartSteak[number][1].toInt()+1).toString()
            view[number].text=cartClass.cartSteak[number][1]
            totalCal()
        }
        else if(type=="pasta"){
            cartClass.cartPasta[number][1]=(cartClass.cartPasta[number][1].toInt()+1).toString()
            view[number].text=cartClass.cartPasta[number][1]
            totalCal()
        }
        else if(type=="wine"){
            cartClass.cartWine[number][1]=(cartClass.cartWine[number][1].toInt()+1).toString()
            view[number].text=cartClass.cartWine[number][1]
            totalCal()
        }
    }

    fun msEvent(number: Int,view: MutableList<TextView>,linearLayout: LinearLayout,customView: View,type:String) {
        if(type=="steak"){
            if(cartClass.cartSteak[number][1].toInt()==1)
            {
                cartClass.cartSteak.removeAt(number)
                linearLayout.removeAllViews()
                initCart()
            }
            else {
                cartClass.cartSteak[number][1] = (cartClass.cartSteak[number][1].toInt() - 1).toString()
                view[number].text = cartClass.cartSteak[number][1]
            }
            totalCal()
        }
        else if(type=="pasta"){
            if(cartClass.cartPasta[number][1].toInt()==1)
            {
                cartClass.cartPasta.removeAt(number)
                linearLayout.removeAllViews()
                initCart()
            }
            else {
                cartClass.cartPasta[number][1] = (cartClass.cartPasta[number][1].toInt() - 1).toString()
                view[number].text = cartClass.cartPasta[number][1]
            }
            totalCal()
        }
        else if(type=="wine"){
            if(cartClass.cartWine[number][1].toInt()==1)
            {
                cartClass.cartWine.removeAt(number)
                linearLayout.removeAllViews()
                initCart()
            }
            else {
                cartClass.cartWine[number][1] = (cartClass.cartWine[number][1].toInt() - 1).toString()
                view[number].text = cartClass.cartWine[number][1]
            }
            totalCal()
        }


    }
}