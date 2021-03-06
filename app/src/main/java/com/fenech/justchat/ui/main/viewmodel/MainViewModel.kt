package com.fenech.justchat.ui.main.viewmodel

import android.widget.Toast
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
    private val debug: DatabaseReference = mainRepository.getDebugRef()
    private var openChat: DatabaseReference? = null

    init {
        //TODO убрать debug
        try {
            debug.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == "on") {
                        subscribeFirebase()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
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
        pushRef.child("authorId").setValue(FirebaseAuth.getInstance().uid)
        pushRef.child("name").setValue(name)
        pushRef.child("author_name")
            .setValue(FirebaseAuth.getInstance().currentUser?.displayName ?: "Гость")
        pushRef.child("url_avatar")
            .setValue(FirebaseAuth.getInstance().currentUser?.photoUrl ?: "")
        pushRef.child("last_message").child("time").setValue(ServerValue.TIMESTAMP)
        pushRef.child("last_message").child("text").setValue("")
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
                dataChat.postValue(dataChatsTemp.sortedBy { it.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun deleteChat(id: String) {
        chatList.child(id).removeValue()
    }

    fun outputMessage(text: String) {
        val userRef = openChat!!.child(FirebaseAuth.getInstance().uid.toString()).push()
        userRef.child("TIMESTAMP").setValue(ServerValue.TIMESTAMP)
        userRef.child("text").setValue(text)
        chatList.child(openChat!!.key.toString()).child("last_message").child("time")
            .setValue(ServerValue.TIMESTAMP)
        chatList.child(openChat!!.key.toString()).child("last_message").child("text")
            .setValue(text)
    }

    fun deleteMessage(id: String) {
        openChat!!.child(FirebaseAuth.getInstance().uid.toString()).child(id).removeValue()
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
                            data.child("authorId").value.toString(),
                            data.child("name").value.toString(),
                            data.child("author_name").value.toString(),
                            data.child("url_avatar").value.toString(),
                            data.child("last_message").child("time").value.toString(),
                            data.child("last_message").child("text").value.toString()
                        )
                    )
                }
                dataChatsList.postValue(dataChatsTemp.sortedByDescending { it.lastMessageTime })
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}