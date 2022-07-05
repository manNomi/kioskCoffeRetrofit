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
import com.bumptech.glide.Glide
import com.example.kiosckoutback.Activity.DataFromFragment
import com.example.kiosckoutback.Activity.MainActivity
import com.example.kiosckoutback.jsonDB.DataBase
import com.example.kiosckoutback.R
import com.example.kiosckoutback.dialogClass

class BeverageFragment : Fragment() {
    var total=arrayOf(0,0,0)

    lateinit var dataList:MutableList<MainActivity.MenuListData>
    val db= DataBase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.main_page_fragment_steak, container, false)
        dataList=db.menuBeverage
        val linearLayout = fragment.findViewById<LinearLayout>(R.id.steakLinear)
        Log.d("result","${dataList} 프라그먼트")
        for (index in 0 until dataList.size) {
            val customLinear = layoutInflater.inflate(R.layout.custom_cart_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.foodText).text = dataList[index].menu_name+"  "+dataList[index].menu_price.toString()
            val image =customLinear.findViewById<ImageView>(R.id.foodImage)
            Glide.with(fragment)
                .load("http://3.39.66.6:3000/"+dataList[index].menu_image)
                .into(image)
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
        val text=dataList[number].menu_name+" 추가되었습니다"
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        val dataInterface = context as DataFromFragment
        dataInterface.sendData("coffe",dataList[number].menu_name,total[number].toString(),dataList[number].menu_price.toString())
    }

    lateinit var newFragment : dialogClass

    fun showDialog() {
        newFragment= dialogClass()
        val fragmentManager=requireActivity().supportFragmentManager
        newFragment.show(fragmentManager, "dialog")
    }

}