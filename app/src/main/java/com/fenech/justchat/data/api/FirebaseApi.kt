package com.fenech.justchat.data.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseApi {

    private val database = Firebase.database
    private val usersRef = database.getReference("users_all")
    private val chatsListRef = database.getReference("chats_list")
    private val chatsRef = database.getReference("chats")

    fun getUsersRef(): DatabaseReference {
        return usersRef
    }

    fun getChatsListRef(): DatabaseReference {
        return chatsListRef
    }

    fun getChatsRef(): DatabaseReference {
        return chatsRef
    }
}