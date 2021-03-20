package com.example.mobimech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mobimech.databinding.ActivityRegistrationBinding

class Registration : AppCompatActivity() {

    lateinit var activityRegistrationBinding:ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityRegistrationBinding=ActivityRegistrationBinding.inflate(layoutInflater)
        val registrationview:View=activityRegistrationBinding.root
        setContentView(registrationview)
        activityRegistrationBinding.loginlink.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }



    }
}