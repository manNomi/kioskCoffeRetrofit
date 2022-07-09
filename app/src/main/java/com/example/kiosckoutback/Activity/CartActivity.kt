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
import com.example.kiosckoutback.DataBase
import com.example.kiosckoutback.MyService
import com.example.kiosckoutback.R
import org.w3c.dom.Text

class CartActivity : AppCompatActivity() {

    lateinit var cartClass: CartClass
    lateinit var myService: MyService
    lateinit var intentService: Intent
    private var doubleBackToExit = false
    var id=""
    var isService = false

//    <뒤로가기 설정
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
//    뒤로가기 설정 >


//    <서비스 연결
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
//    서비스 연결>

//    <서비스

//    <백그라우드로 가는때 - 서비스 연결 >
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        serviceBind()
        ContextCompat.startForegroundService(this, intentService)
    }

//    < 재시작 될때 서비스 실행중이면 끄는 함수 >
    override fun onResume() {
        super.onResume()
        if (isService==true) {
            cartClass = myService?.bindServiceReturn()
            intentService.putExtra("stop", "stop")
            ContextCompat.startForegroundService(this, intentService)
            serviceUnBind()
        }
    }

//    바인드 서비스
    fun serviceBind() {
        intentService = Intent(this, MyService::class.java)
        intentService.putExtra("DATA", cartClass)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

//    바인드 서비스 연결해제
    fun serviceUnBind() {
        if (isService) {
            unbindService(connection)
            isService = false
        }
    }
//    서비스>

//    시작함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_page_activity)
        cartClass = intent.getSerializableExtra("DATA") as CartClass
        id=intent.getStringExtra("id_value")!!
        totalCal()
        initCart()
        initBtn()
    }

//    총합계산함수
    fun totalCal() {
        val total = findViewById<TextView>(R.id.totalCart2)
        total.text = "총합 : ${cartClass.totalCal()}"
    }

//    버튼등록함수
    fun initBtn() {
        val back_btn = findViewById<TextView>(R.id.backBtn)
        back_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("index", "true")
            intent.putExtra("cart", cartClass)
            intent.putExtra("id_value",id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)
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

//    장바구니 정의 함수
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
                )
            }
            linearLayout.addView(customLinear)
        }
    }

//    플러스 버튼 이벤트
    fun psEvent(number: Int, view: MutableList<TextView>, type: String) {
        cartClass.cartCoffe[number].count =
            (cartClass.cartCoffe[number].count.toInt() + 1).toString()
        view[number].text = cartClass.cartCoffe[number].count
        totalCal()
    }

//    마이너스 버튼 이벤트
    fun msEvent(
        number: Int,
        view: MutableList<TextView>,
        linearLayout: LinearLayout,
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