package com.example.kiosckoutback.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.kiosckoutback.Activity.DataFromFragment
import com.example.kiosckoutback.DataBase
import com.example.kiosckoutback.R
import com.example.kiosckoutback.dialogClass

class PastaFragment : Fragment() {

    var total=arrayOf(0,0,0,0)
    val db= DataBase()
    val dataList=db.dataListPasta

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.main_page_fragment_steak, container, false)

        val linearLayout = fragment.findViewById<LinearLayout>(R.id.steakLinear)

        val pastaList= db.pastaList

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
        showDialog()
        Log.d("qwe",newFragment.isCancelable.toString())
        if (newFragment.isCancelable){
                insertEvent(number)
                Log.d("qwe","1235")
        }
    }

    fun insertEvent(number: Int)
    {
        total[number]+=1
        val text=dataList[number][0]+" 추가되었습니다"
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        val dataInterface = context as DataFromFragment
        dataInterface.sendData("pasta",dataList[number][0],total[number].toString(),dataList[number][1])
    }

    lateinit var newFragment :dialogClass

    fun showDialog() {
        newFragment=dialogClass()
        val fragmentManager=requireActivity().supportFragmentManager
        newFragment.show(fragmentManager, "dialog")
    }
}