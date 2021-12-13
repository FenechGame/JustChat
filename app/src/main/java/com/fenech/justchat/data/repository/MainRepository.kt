package com.fenech.justchat.data.repository

import com.fenech.justchat.data.api.FirebaseApi
import com.google.firebase.database.DatabaseReference

class MainRepository(private val firebaseApi: FirebaseApi) {

    fun getUsersRef(): DatabaseReference {
        return firebaseApi.getUsersRef()
    }

    fun getChatsListRef(): DatabaseReference {
        return firebaseApi.getChatsListRef()
    }

    fun getChatsRef(): DatabaseReference {
        return firebaseApi.getChatsRef()
    }
}