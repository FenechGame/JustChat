package com.fenech.justchat.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fenech.justchat.model.Chat

class MainViewModel : ViewModel() {

    private val listChats = MutableLiveData<List<Chat>>()

    fun getChats(): LiveData<List<Chat>> {
        return listChats
    }
}