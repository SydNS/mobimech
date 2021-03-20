package com.example.mobimech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobimech.databinding.ActivityForgotResetpasswordBinding

class ForgotResetpassword : AppCompatActivity() {

    lateinit var activityForgotResetpasswordBinding: ActivityForgotResetpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotResetpasswordBinding= ActivityForgotResetpasswordBinding.inflate(layoutInflater)
        val forgotResetpasswordBindingview=activityForgotResetpasswordBinding.root
        setContentView(forgotResetpasswordBindingview)

        activityForgotResetpasswordBinding.registerlinkresetpass.setOnClickListener {
            startActivity(Intent(this,Registration::class.java))
            finish()
        }
        activityForgotResetpasswordBinding.loginlinkresetpass.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}