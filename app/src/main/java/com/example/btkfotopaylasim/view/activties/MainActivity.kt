package com.example.btkfotopaylasim.view.activties

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.btkfotopaylasim.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val oturum=FirebaseAuth.getInstance().currentUser
        if (oturum==null){
            startActivity(Intent(this@MainActivity,MainActivity2::class.java))
            finish()
        }

    }
}