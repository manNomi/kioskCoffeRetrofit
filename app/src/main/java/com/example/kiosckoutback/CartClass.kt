package com.example.kiosckoutback

import android.util.Log
import java.io.Serializable

class CartClass(
) :Serializable{

    var cartSteak= mutableListOf<Array<String>>()
    var cartPasta= mutableListOf<Array<String>>()
    var cartWine= mutableListOf<Array<String>>()

    var total=0

    fun addCart(type:String,name: String,count:String,price:String){
        if (type=="steak"){
            var cart = arrayOf(name, count, price)
            var sequence=0
            for (index in 0 until cartSteak.size) {
                if (cartSteak[index][0]==cart[0]){
                    cartSteak[index][1] = (cartSteak[index][1].toInt()+1).toString()
                    sequence=100
                    break
                }
            }
            if (sequence==0){
                cartSteak.add(cart)
            }
        }

        else if (type=="pasta"){
            var cart = arrayOf(name, count, price)
            var sequence=0
            for (index in 0 until cartPasta.size) {
                if (cartPasta[index][0]==cart[0]){
                    cartPasta[index][1] = (cartPasta[index][1].toInt()+1).toString()
                    sequence=100
                    break
                }
            }
            if (sequence==0){
                cartPasta.add(cart)
            }
        }
        else if (type=="wine"){
            var cart = arrayOf(name, count, price)
            var sequence=0
            for (index in 0 until cartWine.size) {
                if (cartWine[index][0]==cart[0]){
                    cartWine[index][1] = (cartWine[index][1].toInt()+1).toString()
                    sequence=100
                    break
                }
            }
            if (sequence==0){
                cartWine.add(cart)
            }
        }
    }

    fun totalCal():Int{
        total=0
        for (index in 0 until cartSteak.size){
            total+=cartSteak[index][1].toInt()*cartSteak[index][2].toInt()
        }
        for (index in 0 until cartPasta.size){
            total+=cartPasta[index][1].toInt()*cartPasta[index][2].toInt()
        }
        for (index in 0 until cartWine.size){
            total+=cartWine[index][1].toInt()*cartWine[index][2].toInt()
        }

        return total
    }
}