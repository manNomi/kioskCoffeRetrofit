package com.example.kiosckoutback.Activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kiosckoutback.CartClass
import com.example.kiosckoutback.MyService
import com.example.kiosckoutback.R

class CartActivity : AppCompatActivity() {

    lateinit var cartClass: CartClass
    lateinit var myService: MyService
    lateinit var intentService:Intent


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

    var isService = false
    var connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            isService = true
        }
        override fun onServiceDisconnected(className: ComponentName?) {
            isService = false
        }
    }



    override fun onUserLeaveHint () {
        super.onUserLeaveHint ()
//        intentService= Intent(this, MyService::class.java)
        serviceBind()
        ContextCompat.startForegroundService(this, intentService)
    }

    override fun onRestart() {
        super.onRestart()
        cartClass= myService?.bindServiceReturn()
        intentService.putExtra("stop","stop")
        ContextCompat.startForegroundService(this, intentService)
        serviceUnBind()
    }

    fun serviceBind()
    {
//        Log.d("cart",cartClass.cartSteak[0].name)
        Log.d("cart","cart page hit")
        intentService= Intent(this, MyService::class.java)
        intentService.putExtra("DATA",cartClass)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

    fun serviceUnBind()
    {
        if (isService) {
            unbindService(connection)
            isService = false
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.cart_page_activity)
        cartClass=intent.getSerializableExtra("DATA") as CartClass
        Log.d("cart",cartClass.cartSteak.toString())
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
//            cartClass= myService?.bindServiceReturn()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("index", "true")
            intent.putExtra("cart",cartClass)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)


//            finish()
        }
        val payBtn=findViewById<Button>(R.id.payBtn)
        payBtn.setOnClickListener{
            if (cartClass.totalCal()==0)
            {
                Toast.makeText(this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, ReceiptActivity::class.java)
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
        customLinear.findViewById<TextView>(R.id.cartName).text = cartClass.cartSteak[index].name
        customLinear.findViewById<TextView>(R.id.cartValue).text=cartClass.cartSteak[index].count
            val viewSteak=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArraySteak.add(viewSteak)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArraySteak,"steak")}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArraySteak,linearLayout,customLinear,"steak")}
        linearLayout.addView(customLinear)
        }

        val viewArrayPasta= mutableListOf<TextView>()

        for (index in 0 until cartClass.cartPasta.size){
            val customLinear = layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.cartName).text = cartClass.cartPasta[index].name
            customLinear.findViewById<TextView>(R.id.cartValue).text=cartClass.cartPasta[index].count
            val viewPasta=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArrayPasta.add(viewPasta)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArrayPasta,"pasta")}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArrayPasta,linearLayout,customLinear,"pasta")}
            linearLayout.addView(customLinear)
        }

        val viewArrayWine= mutableListOf<TextView>()

        for (index in 0 until cartClass.cartWine.size){
            val customLinear = layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.cartName).text = cartClass.cartWine[index].name
            customLinear.findViewById<TextView>(R.id.cartValue).text=cartClass.cartWine[index].count
            val viewWine=customLinear.findViewById<TextView>(R.id.cartValue)
            viewArrayWine.add(viewWine)
            customLinear.findViewById<Button>(R.id.plus).setOnClickListener{psEvent(index,viewArrayWine,"wine")}
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener{msEvent(index,viewArrayWine,linearLayout,customLinear,"wine")}
            linearLayout.addView(customLinear)
        }
    }

    fun psEvent(number: Int,view: MutableList<TextView>,type:String) {
        if(type=="steak"){
            cartClass.cartSteak[number].count=(cartClass.cartSteak[number].count.toInt()+1).toString()
            view[number].text=cartClass.cartSteak[number].count
            totalCal()
        }
        else if(type=="pasta"){
            cartClass.cartPasta[number].count=(cartClass.cartPasta[number].count.toInt()+1).toString()
            view[number].text=cartClass.cartPasta[number].count
            totalCal()
        }
        else if(type=="wine"){
            cartClass.cartWine[number].count=(cartClass.cartWine[number].count.toInt()+1).toString()
            view[number].text=cartClass.cartWine[number].count
            totalCal()
        }
    }

    fun msEvent(number: Int,view: MutableList<TextView>,linearLayout: LinearLayout,customView: View,type:String) {
        if(type=="steak"){
            if(cartClass.cartSteak[number].count.toInt()==1)
            {
                cartClass.cartSteak.removeAt(number)
                linearLayout.removeAllViews()
                initCart()
            }
            else {
                cartClass.cartSteak[number].count = (cartClass.cartSteak[number].count.toInt() - 1).toString()
                view[number].text = cartClass.cartSteak[number].count
            }
            totalCal()
        }
        else if(type=="pasta"){
            if(cartClass.cartPasta[number].count.toInt()==1)
            {
                cartClass.cartPasta.removeAt(number)
                linearLayout.removeAllViews()
                initCart()
            }
            else {
                cartClass.cartPasta[number].count = (cartClass.cartPasta[number].count.toInt() - 1).toString()
                view[number].text = cartClass.cartPasta[number].count
            }
            totalCal()
        }
        else if(type=="wine"){
            if(cartClass.cartWine[number].count.toInt()==1)
            {
                cartClass.cartWine.removeAt(number)
                linearLayout.removeAllViews()
                initCart()
            }
            else {
                cartClass.cartWine[number].count = (cartClass.cartWine[number].count.toInt() - 1).toString()
                view[number].text = cartClass.cartWine[number].count
            }
            totalCal()
        }


    }
}