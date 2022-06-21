package com.example.kiosckoutback.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kiosckoutback.Activity.DataFromFragment
import com.example.kiosckoutback.R

class WineFragment : Fragment() {
    var cart= mutableListOf<kotlin.Array<String>>()
    private var total=arrayOf(0,0,0,0)
    val dataList = arrayOf(
        arrayOf("비엔지 비스트로 피노 누아- 55,000","55000"),
        arrayOf("그래트 버지 클래식 컬렉션 쉬라즈 - 49,000","49000"),
        arrayOf("펜폴즈 쿠능가 힐 쉬라즈 카베르네 - 95,000","95000"),
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        val fragment = inflater.inflate(R.layout.main_page_fragment_steak, container, false)


        var sequence=arguments?.getString("index")
        Log.d("ewq","sequance: ${sequence}")
        if (sequence!=null){
            for (index in 0 until sequence!!.toInt()) {
                var getCart=arguments?.getStringArray("cart${index}")
                cart.add(getCart!!)
            }
        }

        val linearLayout = fragment.findViewById<LinearLayout>(R.id.steakLinear)


        val steakList= arrayOf(
            R.mipmap.wine1, R.mipmap.wine2, R.mipmap.wine3
        )
        for (index in 0 until dataList.size) {
            val customLinear = layoutInflater.inflate(R.layout.custom_cart_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.foodText).text = dataList[index][0]
            customLinear.findViewById<ImageView>(R.id.foodImage).setImageResource(steakList[index])
            customLinear.findViewById<LinearLayout>(R.id.steakBtnLinear).setOnClickListener{initEvent(index)}
            linearLayout.addView(customLinear)

        }
        return fragment
    }
    fun initEvent( number: Int){
        dataEvent()
        total[number]+=1
        val text=dataList[number][0]+" 추가되었습니다 \n현재 수량 :"+total[number].toString()
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()

        val dataInterface = context as DataFromFragment
        dataInterface.sendData(dataList[number][0],total[number].toString(),dataList[number][1])

    }
    fun dataEvent(){
        for (index in 0 until cart.size){
            for (index2 in 0 until dataList.size){
                Log.d("ewq","같은값 ${dataList[index2][0]}:${dataList[index2][1]} ${cart[index][0]}:${dataList[index2][1]}")
                if (dataList[index2][0]==cart[index][0]){
                    total[index2]=cart[index][1].toInt()
                }
            }
        }
    }

}