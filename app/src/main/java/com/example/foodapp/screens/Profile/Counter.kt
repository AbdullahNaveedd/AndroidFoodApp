package com.example.foodapp.screens.Profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Counter: ViewModel() {

    var number = MutableLiveData(0)
    fun increment()
    {
        number.value = (number.value ?:0) +1
    }
    fun decrement()
    {
        number.value = (number.value ?:0) -1
    }
}
