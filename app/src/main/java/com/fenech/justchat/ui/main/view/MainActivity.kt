package com.fenech.justchat.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fenech.justchat.R
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}