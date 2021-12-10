package com.fenech.justchat.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fenech.justchat.data.model.Chat
import com.fenech.justchat.data.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val chatsLiveData = MutableLiveData<List<Chat>>()
    private val users: DatabaseReference
    private val chatList: DatabaseReference
    private val chats: DatabaseReference

    init {
        subscribeFirebase()
        users = mainRepository.getUsers()
        chatList = mainRepository.getChatsList()
        chats = mainRepository.getChats()
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().uid?.let {
            mainRepository.getUsers().child(it).setValue("OFFLINE")
        }
    }

    fun getChats(): LiveData<List<Chat>> {
        return chatsLiveData
    }

    fun createNewChat(name: String) {
        chatList.push().child("name").setValue(name)
        chatList.push().child("last_message").setValue("")
    }

    private fun subscribeFirebase() {
        users.child(FirebaseAuth.getInstance().uid.toString())
            .setValue("ONLINE")
        users.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        chatList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatsTemp: ArrayList<Chat> = ArrayList()
                for (data in snapshot.children) {
                    chatsTemp.add(
                        Chat(
                            data.child("name").value.toString(),
                            data.child("last_message").value.toString()
                        )
                    )
                }
                chatsLiveData.postValue(chatsTemp)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}