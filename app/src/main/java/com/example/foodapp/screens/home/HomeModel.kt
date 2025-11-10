package com.example.foodapp.screens.home

data class HomeModel(
    val imageResId:Int? = null,
    val title:String? = null,
    val price:String? = null,
    val location:String? = null,
    val rating:String? = null,
    val reviews:String? = null,
    val description:String? = null,
    val ingredients: List<Ingredient> = emptyList(),
)
