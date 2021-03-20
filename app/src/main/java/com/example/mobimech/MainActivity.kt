package com.example.mobimech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mobimech.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding= ActivityMainBinding.inflate(layoutInflater)
        val mainactivityview:View=activityMainBinding.root
        setContentView(mainactivityview)

        activityMainBinding.loginbtn.setOnClickListener { Toast.makeText(this,"Dean Got it",Toast.LENGTH_LONG).show() }

        activityMainBinding.registerlink.setOnClickListener {
            startActivity(Intent(this,Registration::class.java))
            finish()

        }

    }
}