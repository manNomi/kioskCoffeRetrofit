package com.example.kiosckoutback.Fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor.open
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kiosckoutback.Activity.DataFromFragment
import com.example.kiosckoutback.jsonDB.DataBase
import com.example.kiosckoutback.R
import com.example.kiosckoutback.dialogClass
import kotlinx.android.synthetic.main.main_page_activity.*
import java.io.*
import android.media.Image as Image

class PastaFragment : Fragment() {

    var total=arrayOf(0,0,0,0)
    val db= DataBase


    lateinit var dataList:ArrayList<DataBase.menu>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.main_page_fragment_steak, container, false)
        dataList=db.menuPasta


        val linearLayout = fragment.findViewById<LinearLayout>(R.id.steakLinear)

        for (index in 0 until (dataList ).size) {
            val customLinear = layoutInflater.inflate(R.layout.custom_cart_btn, linearLayout, false)
            customLinear.findViewById<TextView>(R.id.foodText).text = dataList[index].name
            val image =customLinear.findViewById<ImageView>(R.id.foodImage)


            val path2="C:\\Users\\hanmw\\AndroidStudioProjects\\KiosckOutback\\app\\src\\main\\res\\mipmap-mdpi"

//            val inputStream = FileInputStream(path2)
//            val result: Int = inputStream.read()

//            val outputStream = FileOutputStream("image/")
//            outputStream.close()
//            outputStream.close()

//            val path="image/"+ dataList[index].image

            Log.d("qwe",db.pastaImage[index].toString())
            Glide.with(fragment)
                .load(db.pastaImage[index])
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
        val text=dataList[number].name+" 추가되었습니다"
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        val dataInterface = context as DataFromFragment
        dataInterface.sendData("pasta",dataList[number].name,total[number].toString(),dataList[number].price)
    }

    lateinit var newFragment :dialogClass

    fun showDialog() {
        newFragment=dialogClass()
        val fragmentManager=requireActivity().supportFragmentManager
        newFragment.show(fragmentManager, "dialog")
    }

//    fun SaveBitmapToFileCache(strFilePath:String): File {
//
//        var bitmap = BitmapFactory.decodeFile(strFilePath)
//
//        var fileCacheItem = File(strFilePath)
//        var out: OutputStream?= null
//
//        try {
//
//            fileCacheItem.createNewFile()
//            out = FileOutputStream(fileCacheItem)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
//
//        } catch (e:Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                out?.let {
//                    out.close();
//                }
//            } catch (e:IOException) {
//                e.printStackTrace()
//            }
//
//        }
//        return fileCacheItem
//    }
    fun pathToBitmap(path: String?): Bitmap? {
        return try {
            val f = File(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}