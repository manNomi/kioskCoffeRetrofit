package com.example.kiosckoutback
import com.example.kiosckoutback.Activity.MainActivity


object DataBase {
    var menuCoffe= mutableListOf<MainActivity.MenuListData>()
    var menuBeverage=mutableListOf<MainActivity.MenuListData>()

    var serviceBool=false

    var menuDb= mutableListOf<MutableList<MainActivity.MenuListData>>()
    fun initMenus(coffe: MutableList<MainActivity.MenuListData>, beverage: MutableList<MainActivity.MenuListData>){
        menuCoffe =coffe
        menuBeverage =beverage

    }

    fun initMenuDB(menu:MutableList<MainActivity.MenuListData>){
        menuDb.add(menu)
    }
}

