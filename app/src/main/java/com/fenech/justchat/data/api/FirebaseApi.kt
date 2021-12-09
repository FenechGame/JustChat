package com.fenech.justchat.data.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseApi {

    private val database = Firebase.database
    private val myRef = database.reference

    fun getChats(): DatabaseReference {
        return myRef.child("chats")
    }
}