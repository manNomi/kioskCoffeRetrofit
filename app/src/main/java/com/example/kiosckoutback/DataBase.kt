package com.example.kiosckoutback
import com.google.gson.Gson
class DataBase {
    constructor(){
        initMenu()
    }

    val dataListPasta = arrayOf(
        arrayOf("스테이크 투움바 - 29,900","29900"),
        arrayOf("슈림프 아라비아따 - 25,900","25900"),
        arrayOf("알리오 올리오 - 38,900","38900"),
        arrayOf("스파이시 투움바 - 25,900","25900"),
    )

    val dataListSteak = arrayOf(
        arrayOf("갈릭 립아이 46,900","46900"),
        arrayOf("베이비 백립 39,900","39900"),
        arrayOf("기브미 파이브 38,900","38900"),
    )

    val dataListWine = arrayOf(
        arrayOf("비엔지 비스트로 피노 누아- 55,000","55000"),
        arrayOf("그래트 버지 클래식 컬렉션 쉬라즈 - 49,000","49000"),
        arrayOf("펜폴즈 쿠능가 힐 쉬라즈 카베르네 - 95,000","95000"),
    )
    val pastaList= arrayOf(
        R.mipmap.pasta1, R.mipmap.pasta2, R.mipmap.pasta3,R.mipmap.pasta4
    )

    val steakList= arrayOf(
        R.mipmap.steak1, R.mipmap.steak2, R.mipmap.steak3
    )

    val wineList= arrayOf(
        R.mipmap.wine1, R.mipmap.wine2, R.mipmap.wine3
    )

    data class Menu(
        val name:String,
        val price:String,
        val image:Int,
    )
    val pastaMenuList= mutableListOf<Menu>()
    val steakMenuList= mutableListOf<Menu>()
    val wineMenuList= mutableListOf<Menu>()


    data class MenuList(
        val type:String,
    )
    fun initMenu(){

        val gson= Gson()

        for (index in 0 until dataListPasta.size){
        val jsonData="{'name': '${dataListPasta[index][0]}','price':'${dataListPasta[index][1]}','image':'${pastaList[index]}'}"

        val myAccountData=gson.fromJson(jsonData,Menu::class.java)
            pastaMenuList.add(myAccountData)
        }

        for (index in 0 until dataListSteak.size){
            val jsonData="{'name': '${dataListSteak[index][0]}','price':'${dataListSteak[index][1]}','image':'${steakList[index]}'}"

            val myAccountData=gson.fromJson(jsonData,Menu::class.java)
            steakMenuList.add(myAccountData)
        }

        for (index in 0 until dataListWine.size){
            val jsonData="{'name': '${dataListWine[index][0]}','price':'${dataListWine[index][1]}','image':'${wineList[index]}'}"

            val myAccountData=gson.fromJson(jsonData,Menu::class.java)
            wineMenuList.add(myAccountData)
        }






    }

}

