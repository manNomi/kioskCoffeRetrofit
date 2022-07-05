package com.example.kiosckoutback.jsonDB
import android.graphics.Bitmap
import android.util.Log
import com.example.kiosckoutback.Activity.MainActivity
import com.example.kiosckoutback.CategoryData
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


object DataBase {
    var menuCoffe= mutableListOf<MainActivity.MenuListData>()
    var menuBeverage=mutableListOf<MainActivity.MenuListData>()
    fun initMenus(coffe: MutableList<MainActivity.MenuListData>, beverage: MutableList<MainActivity.MenuListData>){
        menuCoffe=coffe
        menuBeverage=beverage
    }
}

