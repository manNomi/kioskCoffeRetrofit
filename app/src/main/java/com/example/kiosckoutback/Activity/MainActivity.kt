package com.example.kiosckoutback.Activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kiosckoutback.*
import com.example.kiosckoutback.Fragment.BeverageFragment
import com.example.kiosckoutback.Fragment.CoffeFragment
import com.example.kiosckoutback.Fragment.LoginFragment
import com.example.kiosckoutback.jsonDB.DataBase
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

interface DataFromFragment{
    fun sendData(receive_type:String,receive_name:String,receive_count: String,receive_pay:String)
}


class MainActivity() : AppCompatActivity(),DataFromFragment {

    lateinit var myService: MyService
    lateinit var intentService:Intent
    lateinit var cartClass: CartClass

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
        Log.d("cart","main page hit")
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

    override fun sendData(
        receive_type: String,
        receive_name: String,
        receive_count: String,
        receive_pay: String
    ) {
        cartClass.addCart(receive_type, receive_name, receive_count, receive_pay)
    }



    var id=""
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db=DataBase

        id=intent.getStringExtra("id_value")!!
        initRetrofit()

        setContentView(R.layout.main_page_activity)

        categoryDataGet()


        var sequance = intent.getStringExtra("index")
        if (sequance != null) {
            cartClass = intent.getSerializableExtra("cart") as CartClass
        } else {
            cartClass = CartClass()
        }
            changeCoffe()
            initEvent()

        }

    fun changeCoffe() {
        val fragmentCoffe = CoffeFragment()
        val bundle = Bundle()
        fragmentCoffe.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentCoffe)
            .commit()
    }

    fun changeBeverage(){
        val fragmentBeverage = BeverageFragment()
        val bundle = Bundle()
        fragmentBeverage.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentBeverage)
            .commit()
    }

    fun btnEvent(number:Int){
        if (number==0){
            changeCoffe()
        }
        else if(number==1){
            changeBeverage()
        }
    }
    fun initEvent() {
        val cart_btn = findViewById<TextView>(R.id.cartMoveBtn)
        cart_btn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("DATA", cartClass)

            intent.putExtra("id_value", id)

            intent.addFlags(FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)
//            finish()
        }
        val history_btn = findViewById<TextView>(R.id.historyBtn)
        history_btn.setOnClickListener {
        initRecipt()
        }
    }



    lateinit var db:DataBase
    lateinit var retrofit: Retrofit
    lateinit var retrofitHttp: RetrofitService


    fun initRetrofit() {
        retrofit= RetrofitClient.initRetrofit()
        retrofitHttp=retrofit!!.create(RetrofitService::class.java)
    }

    data class Category(
        val seq: Int,
        val category_name: String,
        val lang: String
    )


    fun initCategory(data: MutableList<String>){
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutCustom)
        for (index in 0 until data.size) {
            val customLinear = layoutInflater.inflate(R.layout.custom_category_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.category).text = data[index]
            customLinear.findViewById<TextView>(R.id.category).setOnClickListener{
                Log.d("qwe","버튼클릭됨")
                btnEvent(index)
            }
            linearLayout.addView(customLinear)
        }
    }

    var category= mutableListOf<String>()

    fun categoryDataGet(){
        retrofitHttp.getAccountCategory("kr").
        enqueue(object: Callback<CategoryData> {
            //                통신 실패함수
            override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                Log.d("result","Request fail : ${t}")
//                    통신 연결과 실패했다면 실패한 이유도 나옴
            }
            //                  통신성공함수
            override fun onResponse(call: Call<CategoryData>, response: Response<CategoryData>) {
                Log.d("result","Request Success }")

                if (response.body()!!.success){
                    Log.d("result","데이터 ${response.body()!!.data}")
//                    db.initCategory(response.body()!!.data.toString())
                    val gson= Gson()
                    val data = gson.fromJson(response.body()!!.data[0], Category::class.java)
                    val data2 = gson.fromJson(response.body()!!.data[1], Category::class.java)
                    category.add(data.category_name)
                    category.add(data2.category_name)
                    initCategory(category)
                    MenuDataGet(category)

                    Log.d("result","카테고리 : ${category[0] + category[1]}")
                }
                else{
                    Log.d("result",response.body()!!.message)

                }
            }
        }
        )

    }



    data class MenuListData(
        var menu_name: String,
        var menu_price: Int,
        var menu_image: String
    )

    var menuDataCoffe= mutableListOf<MenuListData>()
    var menuDataBeverage= mutableListOf<MenuListData>()

    fun MenuDataGet(categoty:MutableList<String>){
        retrofitHttp.getAccountMenu("kr",categoty[0]).
        enqueue(object: Callback<MenuData> {
            //                통신 실패함수
            override fun onFailure(call: Call<MenuData>, t: Throwable) {
                Log.d("result","Request fail : ${t}")
//                    통신 연결과 실패했다면 실패한 이유도 나옴
            }
            //                  통신성공함수
            override fun onResponse(call: Call<MenuData>, response: Response<MenuData>) {
                Log.d("result","Request Success }")

                if (response.body()!!.success){
                    Log.d("result","데이터 ${response.body()!!.data}")
//                    db.initCategory(response.body()!!.data.toString())

                    for (index in 0 until response.body()!!.data.size()) {
                        val gson = Gson()
                        val menu = gson.fromJson(response.body()!!.data[index], MenuListData::class.java)
                        menuDataCoffe.add(menu)
                    }
                    initMenus()

                }
                else{
                    Log.d("result",response.body()!!.message)

                }
            }
        }
        )

        retrofitHttp.getAccountMenu("kr",categoty[1]).
        enqueue(object: Callback<MenuData> {
            //                통신 실패함수
            override fun onFailure(call: Call<MenuData>, t: Throwable) {
                Log.d("result","Request fail : ${t}")
//                    통신 연결과 실패했다면 실패한 이유도 나옴
            }
            //                  통신성공함수
            override fun onResponse(call: Call<MenuData>, response: Response<MenuData>) {
                Log.d("result","Request Success }")

                if (response.body()!!.success){
                    Log.d("result","데이터 ${response.body()!!.data}")
//                    db.initCategory(response.body()!!.data.toString())

                    for (index in 0 until response.body()!!.data.size()) {
                        val gson = Gson()
                        val menu = gson.fromJson(response.body()!!.data[index], MenuListData::class.java)
                        menuDataBeverage.add(menu)
                    }
                    initMenus()

                }
                else{
                    Log.d("result",response.body()!!.message)

                }
            }
        }
        )

    }
    fun initMenus(){
        db.initMenus(menuDataCoffe,menuDataBeverage)
        changeCoffe()
    }

    data class dataHistory(
        var seq: Int,
        var id:String,
        var total_price: Int,
        var order_list: ArrayList<orderData>,

        )

    data class orderData(
        var count:Int,
        var name : String,
        var sum_price:Int
    )

    var menuHistory= mutableListOf<dataHistory>()

    fun initRecipt(){
        val id=id
        retrofitHttp.getHistory(id).
        enqueue(object: Callback<GetHistroy> {
            override fun onFailure(call: Call<GetHistroy>, t: Throwable) {
                Log.d("result","Request fail : ${t}")
            }
            override fun onResponse(call: Call<GetHistroy>, response: Response<GetHistroy>) {
                Log.d("result","Request Success }")

                if (response.body()!!.success){
                    menuHistory.clear()
                    Log.d("result","주문기록 ${response.body()!!.data}")
                    val gson = Gson()
                    for (index in 0 until response.body()!!.data.size()) {
                        val menu = gson.fromJson(response.body()!!.data[index], dataHistory::class.java)
                        Log.d("result","제이슨 파일 ${menu}")

                        menuHistory.add(menu)
                    }

                    Log.d("result","기록된 메뉴는 ${menuHistory}")
                    var text=""
                    text+="id: ${menuHistory[0].id}\n"
                    for (index in 0 until  menuHistory.size){
                        text+=(index+1).toString()+"번째 주문\n"
                        for (index2 in 0 until  menuHistory[index].order_list.size) {
                            text += "메뉴 ${menuHistory[index].order_list[index2].name} 개수 ${menuHistory[index].order_list[index2].count} 가격 ${menuHistory[index].order_list[index2].sum_price}\n"
                        }
                        text+="total: ${menuHistory[index].total_price} \n"
                    }
                    showDialog(text)

                }
                else{
                    Log.d("result",response.body()!!.message)
                }
            }
        }
        )
    }
    fun showDialog(text:String) {
        var newFragment= dialog()
        val bundle = Bundle()
        bundle.putString("text",text)
        newFragment.arguments = bundle
        val fragmentManager=supportFragmentManager
        newFragment.show(fragmentManager, "dialog")
    }
}

