package com.example.kiosckoutback.Activity

import android.content.Intent
import android.os.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentTransaction
import com.example.kiosckoutback.CartClass
import com.example.kiosckoutback.Fragment.PastaFragment
import com.example.kiosckoutback.Fragment.SteakFragment
import com.example.kiosckoutback.Fragment.WineFragment
import com.example.kiosckoutback.R
import com.example.kiosckoutback.dialogClass

interface DataFromFragment{
    fun sendData(receive_type:String,receive_name:String,receive_count: String,receive_pay:String)
}
class MainActivity() : AppCompatActivity(),DataFromFragment {
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

    lateinit var cartClass: CartClass

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
        setContentView(R.layout.main_page_activity)

//        var sequance=intent.getStringExtra("index")
//        if (sequance!=null){
//            for (index in 0 until sequance!!.toInt()) {
//                var getCart=intent.getStringArrayExtra("cart${index}")
//                cart.add(getCart!!)}
//        }

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

//            bundle.putString("index", cart.size.toString())
//            var text= mutableListOf<String>()
//            for(index in 0 until cart.size) {
//                text.add("cart" + "${index}")
//                bundle.putStringArray(text[index], cart[index])
//            }

            bundle.putSerializable("cart", cartClass)

            fragmentWine.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentWine)
                .commit()
        }

        val cart_btn = findViewById<Button>(R.id.cartMoveBtn)
        cart_btn.setOnClickListener {
//            repeatCart()
            val intent = Intent(this, CartActivity::class.java)


//            intent.putExtra("index", cart.size.toString())
//
//            var text= mutableListOf<String>()
//            for(index in 0 until cart.size) {
//                text.add("cart" + "${index}")
//                intent.putExtra(text[index], cart[index])
//            }

            intent.putExtra("DATA", cartClass)

            startActivity(intent)
            finish()
        }
    }

//    fun repeatCart(){
//        for (i in 0 until cart.size){
//        var sequence=cart.size
//        for (index in 0 until sequence){
//            for (index2 in 0 until sequence){
//                if (cart[index][0]==cart[index2][0]){
//                    if(cart[index][1].toInt()<cart[index2][1].toInt()) {
//                        cart.removeAt(index)
//                        sequence-=1
//                        break
//                    }
//                    else if(cart[index][1].toInt()==cart[index2][1].toInt())
//                    { }
//                    else {
//                        cart.removeAt(index2)
//                        sequence-=1
//                        break
//                        }
//                    }
//                }
//            }
//        }
//    }


}

