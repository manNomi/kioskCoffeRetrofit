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
import org.w3c.dom.Text

class CartActivity : AppCompatActivity() {

    lateinit var cartClass: CartClass
    lateinit var myService: MyService
    lateinit var intentService: Intent


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


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
//        intentService= Intent(this, MyService::class.java)
        serviceBind()
        ContextCompat.startForegroundService(this, intentService)
    }

    override fun onRestart() {
        super.onRestart()
        cartClass = myService?.bindServiceReturn()
        intentService.putExtra("stop", "stop")
        ContextCompat.startForegroundService(this, intentService)
        serviceUnBind()
    }

    fun serviceBind() {
//        Log.d("cart",cartClass.cartSteak[0].name)
        Log.d("cart", "cart page hit")
        intentService = Intent(this, MyService::class.java)
        intentService.putExtra("DATA", cartClass)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

    fun serviceUnBind() {
        if (isService) {
            unbindService(connection)
            isService = false
        }
    }

    var id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.cart_page_activity)
        cartClass = intent.getSerializableExtra("DATA") as CartClass

        id=intent.getStringExtra("id_value")!!

        Log.d("cart", cartClass.cartCoffe.toString())
        totalCal()
        initCart()
        initEvent()
    }

    fun totalCal() {
        val total = findViewById<TextView>(R.id.totalCart2)
        total.text = "총합 : ${cartClass.totalCal()}"
    }

    fun initEvent() {
        val back_btn = findViewById<TextView>(R.id.backBtn)
        back_btn.setOnClickListener {
//            cartClass= myService?.bindServiceReturn()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("index", "true")
            intent.putExtra("cart", cartClass)
            intent.putExtra("id_value",id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)


//            finish()
        }
        val payBtn = findViewById<TextView>(R.id.payBtn)
        payBtn.setOnClickListener {
            if (cartClass.totalCal() == 0) {
                Toast.makeText(this, "장바구니가 비어있습니다", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra("cart", cartClass)
                intent.putExtra("id_value",id)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
                startActivity(intent)
                finish()
            }
        }
    }

    fun initCart() {

        val linearLayout = findViewById<LinearLayout>(R.id.customCart)
        val viewArraySteak = mutableListOf<TextView>()
        for (index in 0 until cartClass.cartCoffe.size) {
            val customLinear =
                layoutInflater.inflate(R.layout.custom_image_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.cartName).text =
                cartClass.cartCoffe[index].name + " " + cartClass.cartCoffe[index].price
            customLinear.findViewById<TextView>(R.id.cartValue).text =
                cartClass.cartCoffe[index].count
            val viewCoffe = customLinear.findViewById<TextView>(R.id.cartValue)
            viewArraySteak.add(viewCoffe)
            customLinear.findViewById<Button>(R.id.plus)
                .setOnClickListener { psEvent(index, viewArraySteak, "steak") }
            customLinear.findViewById<Button>(R.id.minus).setOnClickListener {
                msEvent(
                    index,
                    viewArraySteak,
                    linearLayout,
                    customLinear,
                    "coffe"
                )
            }
            linearLayout.addView(customLinear)
        }
    }


        fun psEvent(number: Int, view: MutableList<TextView>, type: String) {
            cartClass.cartCoffe[number].count =
                (cartClass.cartCoffe[number].count.toInt() + 1).toString()
            view[number].text = cartClass.cartCoffe[number].count
            totalCal()
        }

        fun msEvent(
            number: Int,
            view: MutableList<TextView>,
            linearLayout: LinearLayout,
            customView: View,
            type: String
        ) {
                if (cartClass.cartCoffe[number].count.toInt() == 1) {
                    cartClass.cartCoffe.removeAt(number)
                    linearLayout.removeAllViews()
                    initCart()
                } else {
                    cartClass.cartCoffe[number].count =
                        (cartClass.cartCoffe[number].count.toInt() - 1).toString()
                    view[number].text = cartClass.cartCoffe[number].count
                }
                totalCal()
        }
}