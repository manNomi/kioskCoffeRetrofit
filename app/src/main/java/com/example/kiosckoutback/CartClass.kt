package com.example.kiosckoutback

import java.io.Serializable

class CartClass(
) :Serializable{
    var cartCoffe= mutableListOf<List>()
    var total=0
    data class List (
        val name:String,
        var count:String,
        val price:String
        ): Serializable

    fun addCart(type:String,name: String,count:String,price:String){
        var cart=List(name, count, price)
        var sequence=0
        for (index in 0 until cartCoffe.size) {
            if (cartCoffe[index].name==cart.name){
                cartCoffe[index].count = (cartCoffe[index].count.toInt()+1).toString()
                sequence=100
                break
            }
        }
        if (sequence==0){
            cartCoffe.add(cart)
        }
    }

    fun totalCal():Int{
        total=0
        for (index in 0 until cartCoffe.size){
            total+=cartCoffe[index].count.toInt()*cartCoffe[index].price.toInt()
        }
        return total
    }
}