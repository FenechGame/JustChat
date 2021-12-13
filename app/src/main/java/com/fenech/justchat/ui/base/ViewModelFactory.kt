package com.fenech.justchat.ui.base

import androidx.lifecycle.ViewModelProvider
import com.fenech.justchat.data.api.FirebaseApi
import com.fenech.justchat.data.repository.MainRepository
import com.fenech.justchat.ui.main.viewmodel.MainViewModel

class ViewModelFactory(private val firebaseApi: FirebaseApi) : ViewModelProvider.Factory {

    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(firebaseApi)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}