package com.example.kiosckoutback.Activity

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosckoutback.Fragment.PastaFragment
import com.example.kiosckoutback.Fragment.SteakFragment
import com.example.kiosckoutback.Fragment.WineFragment
import com.example.kiosckoutback.R
import org.w3c.dom.Text

interface DataFromFragment{
    fun sendData(receive_name:String,receive_value: String,receive_pay:String)
}
class MainActivity() : AppCompatActivity(),DataFromFragment  {

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

    private var cart= mutableListOf<Array<String>>()


    override fun sendData(receive_name: String, receive_value: String,receive_pay:String) {


        val cartListData=arrayOf(receive_name,receive_value,receive_pay)

        cart.add(cartListData)

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_activity)

        var sequance=intent.getStringExtra("index")
        if (sequance!=null){
            for (index in 0 until sequance!!.toInt()) {
                var getCart=intent.getStringArrayExtra("cart${index}")
                cart.add(getCart!!)}
        }



        firstInitStaek()
        initEvent()

    }

    fun firstInitStaek(){
        val fragmentSteak = SteakFragment()

        val bundle=Bundle()
        bundle.putString("index", cart.size.toString())
        var text= mutableListOf<String>()
        for(index in 0 until cart.size) {
            text.add("cart" + "${index}")
            bundle.putStringArray(text[index], cart[index])
        }
        fragmentSteak.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentSteak).commit()

    }
    fun initEvent() {
        val steak_btn = findViewById<Button>(R.id.steakBtn)
        steak_btn.setOnClickListener {
            val fragmentSteak = SteakFragment()

            val bundle=Bundle()
            bundle.putString("index", cart.size.toString())
            var text= mutableListOf<String>()
            for(index in 0 until cart.size) {
                text.add("cart" + "${index}")
                bundle.putStringArray(text[index], cart[index])
            }
            fragmentSteak.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentSteak).commit()
        }

        val pasta_btn = findViewById<Button>(R.id.pastaBtn)
        pasta_btn.setOnClickListener {
            val fragmentPasta = PastaFragment()
            val bundle=Bundle()
            bundle.putString("index", cart.size.toString())
            var text= mutableListOf<String>()
            for(index in 0 until cart.size) {
                text.add("cart" + "${index}")
                bundle.putStringArray(text[index], cart[index])
            }
            fragmentPasta.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentPasta).commit()
        }

        val wine_btn = findViewById<Button>(R.id.wineBtn)
        wine_btn.setOnClickListener {
            val fragmentWine = WineFragment()

            val bundle=Bundle()
            bundle.putString("index", cart.size.toString())
            var text= mutableListOf<String>()
            for(index in 0 until cart.size) {
                text.add("cart" + "${index}")
                bundle.putStringArray(text[index], cart[index])
            }
            fragmentWine.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentWine).commit()
        }

        val cart_btn = findViewById<Button>(R.id.cartMoveBtn)
        cart_btn.setOnClickListener{
            repeatCart()
//            clearCart()
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("index", cart.size.toString())
            var text= mutableListOf<String>()
            for(index in 0 until cart.size) {
                text.add("cart" + "${index}")
                intent.putExtra(text[index], cart[index])
            }

            startActivity(intent)
            finish()
        }

    }

//    fun clearCart(){
//        var sequence=cart.size
//        for (index in 0 until sequence){
//            if (cart[index][1].toInt()>1){
//                Log.d("res",cart[index][0])
//                var delArray=arrayOf(cart[index][0],(cart[index][1].toInt()-1).toString(),cart[index][2])
//                for (index2 in 0 until sequence){
//                    if (cart[index2][0]==delArray[0]&&cart[index2][1]==delArray[1])
//                    {
//                        Log.d("res","인덱스 ${index2}")
//                        cart.removeAt(index2)
//                        sequence-=1
//                        break
//                    }
//                }
//                    }
//                }
//            }

    fun repeatCart(){
        for (i in 0 until cart.size){

        var sequence=cart.size
        for (index in 0 until sequence){
            for (index2 in 0 until sequence){
                if (cart[index][0]==cart[index2][0]){
                    Log.d("ewq","${cart[index][1]}  ${cart[index2][1]}")
                    if(cart[index][1].toInt()<cart[index2][1].toInt()) {
                        Log.d("ewq","삭제 1 ${cart[index][0]}  ${cart[index][1]}")
                        cart.removeAt(index)
                        sequence-=1
                        break
                    }
                    else if(cart[index][1].toInt()==cart[index2][1].toInt())
                    {
//                        Log.d("ewq","증복  ${cart[index][0]}  ${cart[index][1]}")
//                        cart[index][1]=(cart[index][1].toInt()+cart[index2][1].toInt()).toString()
//                        cart.removeAt(index2)
//                        sequence-=1
//                        break
                    }
                    else {
                        Log.d("ewq","삭제 2 ${cart[index2][0]}  ${cart[index2][1]}")
                        cart.removeAt(index2)
                        sequence-=1
                        break
                    }
                    }
            }
        }
    }
    }
    }

