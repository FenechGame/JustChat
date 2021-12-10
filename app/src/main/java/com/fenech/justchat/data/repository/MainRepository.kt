package com.fenech.justchat.data.repository

import com.fenech.justchat.data.api.FirebaseApi
import com.google.firebase.database.DatabaseReference

class MainRepository(private val firebaseApi: FirebaseApi) {

    fun getUsers(): DatabaseReference {
        return firebaseApi.getUsers()
    }

    fun getChatsList(): DatabaseReference {
        return firebaseApi.getChatsList()
    }

    fun getChats(): DatabaseReference {
        return firebaseApi.getChats()
    }
}