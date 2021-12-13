package com.fenech.justchat.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fenech.justchat.data.model.DataChat
import com.fenech.justchat.data.model.DataMessage
import com.fenech.justchat.data.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.NullPointerException
import kotlin.jvm.Throws

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val dataChatsList = MutableLiveData<List<DataChat>>()
    private val dataChat = MutableLiveData<List<DataMessage>>()
    private val users: DatabaseReference = mainRepository.getUsersRef()
    private val chatList: DatabaseReference = mainRepository.getChatsListRef()
    private val chats: DatabaseReference = mainRepository.getChatsRef()
    private var openChat: DatabaseReference? = null

    init {
        try {
            subscribeFirebase()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (openChat != null) {
            openChat = null
            return
        }
        FirebaseAuth.getInstance().uid?.let {
            mainRepository.getUsersRef().child(it).setValue("OFFLINE")
        }
    }

    fun getChatsListLiveData(): LiveData<List<DataChat>> {
        return dataChatsList
    }

    fun getChatLiveData(): LiveData<List<DataMessage>> {
        return dataChat
    }

    fun createNewChat(name: String) {
        val pushRef = chatList.push()
        pushRef.child("name").setValue(name)
        pushRef.child("last_message").setValue("")
    }

    fun openChat(id: String) {
        openChat = chats.child(id)
        openChat!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataChatsTemp: ArrayList<DataMessage> = ArrayList()
                for (userMessages in snapshot.children) {
                    for (data in userMessages.children) {
                        dataChatsTemp.add(
                            DataMessage(
                                userMessages.key.toString(),
                                data.key.toString(),
                                "name user",
                                data.child("text").value.toString(),
                                data.child("TIMESTAMP").value.toString(),
                            )
                        )
                    }
                }
                dataChat.postValue(dataChatsTemp)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun outputMessage(text: String) {
        val userRef = openChat!!.child(FirebaseAuth.getInstance().uid.toString()).push()
        userRef.child("TIMESTAMP").setValue(ServerValue.TIMESTAMP)
        userRef.child("text").setValue(text)
    }

    @Throws(NullPointerException::class)
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
                val dataChatsTemp: ArrayList<DataChat> = ArrayList()
                for (data in snapshot.children) {
                    dataChatsTemp.add(
                        DataChat(
                            data.key.toString(),
                            data.child("name").value.toString(),
                            data.child("last_message").value.toString()
                        )
                    )
                }
                dataChatsList.postValue(dataChatsTemp)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}