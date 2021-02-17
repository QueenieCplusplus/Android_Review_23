package com.katepatty.kateswifi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


// Tab 功能 -------------------------

class PageViewModel : ViewModel() {

    private val _string = MutableLiveData<String>()
    val text: LiveData<String> = Transformations.map(_string) {
        "Change mode is now at: $it"
    }

    fun setSt(st: String) {
        _string.value = st
    }
}