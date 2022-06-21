package com.example.kiosckoutback.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.kiosckoutback.Activity.DataFromFragment
import com.example.kiosckoutback.R
import org.w3c.dom.Text
import java.lang.reflect.Array
import java.util.*

class PastaFragment : Fragment() {


    var total=arrayOf(0,0,0,0)
    var cart= mutableListOf<kotlin.Array<String>>()

    val dataList = arrayOf(
        arrayOf("스테이크 투움바 - 29,900","29900"),
        arrayOf("슈림프 아라비아따 - 25,900","25900"),
        arrayOf("알리오 올리오 - 38,900","38900"),
        arrayOf("스파이시 투움바 - 25,900","25900"),
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

        val pastaList= arrayOf(
            R.mipmap.pasta1, R.mipmap.pasta2, R.mipmap.pasta3,R.mipmap.pasta4
        )
        for (index in 0 until dataList.size) {
            val customLinear = layoutInflater.inflate(R.layout.custom_cart_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.foodText).text = dataList[index][0]
            customLinear.findViewById<ImageView>(R.id.foodImage).setImageResource(pastaList[index])
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