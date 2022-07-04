package com.example.kiosckoutback.Activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kiosckoutback.CartClass
import com.example.kiosckoutback.Fragment.PastaFragment
import com.example.kiosckoutback.Fragment.SteakFragment
import com.example.kiosckoutback.Fragment.WineFragment
import com.example.kiosckoutback.MyService
import com.example.kiosckoutback.R
import com.example.kiosckoutback.jsonDB.DataBase
import java.io.InputStream

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
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMenu()


            setContentView(R.layout.main_page_activity)
        var sequance = intent.getStringExtra("index")
        if (sequance != null) {
            cartClass = intent.getSerializableExtra("cart") as CartClass
        } else {
            cartClass = CartClass()
        }
        firstInitStaek()
        initEvent()
    }

    fun firstInitStaek() {
        val fragmentSteak = SteakFragment()
        val bundle = Bundle()

        bundle.putSerializable("cart", cartClass)

        fragmentSteak.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentSteak)
            .commit()
    }

    fun initEvent() {
        val steak_btn = findViewById<Button>(R.id.steakBtn)
        steak_btn.setOnClickListener {

            val fragmentSteak = SteakFragment()
            val bundle = Bundle()

            bundle.putSerializable("cart", cartClass)
            fragmentSteak.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentSteak)
                .commit()
        }

        val pasta_btn = findViewById<Button>(R.id.pastaBtn)
        pasta_btn.setOnClickListener {
            val fragmentPasta = PastaFragment()
            val bundle = Bundle()

            bundle.putSerializable("cart", cartClass)

            fragmentPasta.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentPasta)
                .commit()
        }

        val wine_btn = findViewById<Button>(R.id.wineBtn)
        wine_btn.setOnClickListener {
            val fragmentWine = WineFragment()
            val bundle = Bundle()

            bundle.putSerializable("cart", cartClass)
            fragmentWine.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentWine)
                .commit()
        }

        val cart_btn = findViewById<Button>(R.id.cartMoveBtn)
        cart_btn.setOnClickListener {

            val intent = Intent(this, CartActivity::class.java)

            intent.putExtra("DATA", cartClass)
            intent.addFlags(FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)
//            finish()
        }
    }

    lateinit var db:DataBase
    lateinit var pastaPicture:MutableList<Bitmap>
    lateinit var steakPicture:MutableList<Bitmap>
    lateinit var winePicture:MutableList<Bitmap>

    fun initMenu(){
        val jsonString = assets.open("data.json").reader().readText()
        db=DataBase
        db.initMenu(jsonString)
        initPicture()
    }

    fun initPicture(){
        pastaPicture=mutableListOf<Bitmap>()

        for (index  in 0 until db.menuPasta.size) {
            val image = assets.open(db.menuPasta[index].image)
            var bitmap: Bitmap = BitmapFactory.decodeStream(image)
            pastaPicture.add(bitmap)
        }

        steakPicture= mutableListOf<Bitmap>()
        for (index  in 0 until db.menuSteak.size) {
            val image = assets.open(db.menuSteak[index].image)
            var bitmap: Bitmap = BitmapFactory.decodeStream(image)
            steakPicture.add(bitmap)
        }

        winePicture= mutableListOf<Bitmap>()
        for (index  in 0 until db.menuWine.size) {
            val image = assets.open(db.menuWine[index].image)
            var bitmap: Bitmap = BitmapFactory.decodeStream(image)
            winePicture.add(bitmap)
        }

        db.initImage(pastaPicture,steakPicture,winePicture)

    }

}

