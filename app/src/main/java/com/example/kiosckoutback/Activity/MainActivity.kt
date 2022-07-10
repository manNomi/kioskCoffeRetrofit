package com.example.kiosckoutback.Activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kiosckoutback.*
import com.example.kiosckoutback.DataBase
import com.example.kiosckoutback.DataBase.initMenuDB
import com.example.kiosckoutback.Fragment.ChangeFragment
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
    var id=""
    lateinit var db: DataBase
    lateinit var retrofit: Retrofit
    lateinit var retrofitHttp: RetrofitService

    var isService = false
    var category= mutableListOf<String>()
    var menuHistory= mutableListOf<dataHistory>()

    data class MenuListData(
        var menu_name: String,
        var menu_price: Int,
        var menu_image: String
    )
    data class Category(
        val seq: Int,
        val category_name: String,
        val lang: String
    )
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


//    서비스
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

//  백그라운드로 넘어갈때 - 서비스 연결
    override fun onUserLeaveHint () {
        super.onUserLeaveHint ()
        serviceBind()
        intentService.putExtra("test","main")
        ContextCompat.startForegroundService(this, intentService)
    }

//    재시작 - 서비스가 연결되있을경우 서비스 해제
    override fun onResume() {
        super.onResume()
        if (isService) {
            cartClass = myService?.bindServiceReturn()
            intentService.putExtra("test","main")
            intentService.putExtra("stop", "stop")
            ContextCompat.startForegroundService(this, intentService)
            serviceUnBind()
        }
    }

//    서비스 연결 함수
    fun serviceBind()
    {
        intentService= Intent(this, MyService::class.java)
        intentService.putExtra("DATA",cartClass)

        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

//    서비스 연결이 되있을때 호출되면 해제
    fun serviceUnBind()
    {
        if (isService) {
            unbindService(connection)
            isService = false
        }
    }

//    데이터 받는곳
    override fun sendData(
        receive_type: String,
        receive_name: String,
        receive_count: String,
        receive_pay: String
    ) {
        cartClass.addCart(receive_type, receive_name, receive_count, receive_pay)
    }

//    시작함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db= DataBase
        if (intent.getStringExtra("id_value")!=null){
        id=intent.getStringExtra("id_value")!!
        }
        initRetrofit()
        categoryDataGet()

        setContentView(R.layout.main_page_activity)
        var sequance = intent.getStringExtra("index")
        if (sequance != null) {
            cartClass = intent.getSerializableExtra("cart") as CartClass
        } else {
            cartClass = CartClass()
        }
            initEvent()
    }

// 프라그먼트 교체

    fun changeMenu(menuType:Int){
        Log.d("men메뉴",menuType.toString())
        val fragment = ChangeFragment()
        val bundle = Bundle()
        bundle.putString("type",menuType.toString())
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
            .commit()
    }

    fun initEvent() {
        val cart_btn = findViewById<TextView>(R.id.cartMoveBtn)
        cart_btn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("DATA", cartClass)
            intent.putExtra("id_value", id)
            intent.addFlags(FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)
        }
        val history_btn = findViewById<TextView>(R.id.historyBtn)
        history_btn.setOnClickListener {
        initRecipt()
        }
    }

    fun initRetrofit() {
        retrofit= RetrofitClient.initRetrofit()
        retrofitHttp=retrofit!!.create(RetrofitService::class.java)
    }


    fun categoryDataGet(){
        Log.d("qwe","카테고리 시작")
        retrofitHttp.getAccountCategory("kr").
        enqueue(object: Callback<CategoryData> {
            override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                Log.d("qwe","통신 실패")
            }
            override fun onResponse(call: Call<CategoryData>, response: Response<CategoryData>) {
                Log.d("qwe",response.body()!!.success.toString())

                if (response.body()!!.success){
                    Log.d("qwe","카테고리 개수 ${response.body()!!.data.size()}")

                    val gson= Gson()
                    for (index in 0 until response.body()!!.data.size()){
                        val categoryName = gson.fromJson(response.body()!!.data[index], Category::class.java)
                        category.add(categoryName.category_name)
                        Log.d("qwe","카테고리 종류 ${categoryName.category_name}")

                    }
                    initCategory(category)
                    MenuDataGet(category)
                    Log.d("qwe","카테고리")
                }
            }
        }
        )
    }

    fun initCategory(data: MutableList<String>){
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutCustom)
        for (index in 0 until data.size) {
            val customLinear = layoutInflater.inflate(R.layout.custom_category_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.category).text = data[index]
            customLinear.findViewById<TextView>(R.id.category).setOnClickListener{
                changeMenu(index)
            }
            linearLayout.addView(customLinear)
        }
    }

    fun MenuDataGet(categoty:MutableList<String>){
        Log.d("qwe","메뉴시작")

        for (index in 0 until categoty.size){
        retrofitHttp.getAccountMenu("kr",categoty[index]).
        enqueue(object: Callback<MenuData> {
            override fun onFailure(call: Call<MenuData>, t: Throwable) {
            }
            override fun onResponse(call: Call<MenuData>, response: Response<MenuData>) {
                if (response.body()!!.success){
                    var menuData= mutableListOf<MenuListData>()
                    for (index in 0 until response.body()!!.data.size()) {
                        val gson = Gson()
                        val menu = gson.fromJson(response.body()!!.data[index], MenuListData::class.java)
                        menuData.add(menu)
                    }
                    initMenus(menuData)
                    Log.d("qwe",menuData.toString())
                }
            }
        }) }

    }
    fun initMenus(menus:MutableList<MenuListData>){
        initMenuDB(menus)
        changeMenu(0)
    }

    fun initRecipt(){
        val id=id
        retrofitHttp.getHistory(id).
        enqueue(object: Callback<GetHistroy> {
            override fun onFailure(call: Call<GetHistroy>, t: Throwable) {
            }
            override fun onResponse(call: Call<GetHistroy>, response: Response<GetHistroy>) {
                if (response.body()!!.success){
                    menuHistory.clear()
                    val gson = Gson()
                    for (index in 0 until response.body()!!.data.size()) {
                        val menu = gson.fromJson(response.body()!!.data[index], dataHistory::class.java)
                        menuHistory.add(menu)
                    }
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

