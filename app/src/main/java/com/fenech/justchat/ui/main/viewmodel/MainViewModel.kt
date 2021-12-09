package com.fenech.justchat.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fenech.justchat.data.model.Chat
import com.fenech.justchat.data.repository.MainRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val chats = MutableLiveData<List<Chat>>()

    init {
        subscribeChats()
    }

    private fun subscribeChats() {
        mainRepository.getChats().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                print("TEST ")
                val chatsTemp: ArrayList<Chat> = ArrayList()
                for (chat in dataSnapshot.children) {
                    chatsTemp.add(Chat(chat.key.toString(), chat.value.toString()))
                }
                chats.postValue(chatsTemp)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getChats(): LiveData<List<Chat>> {
        return chats
    }
}