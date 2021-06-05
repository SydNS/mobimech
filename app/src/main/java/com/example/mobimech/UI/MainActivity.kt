@file:Suppress("DEPRECATION")

package com.example.mobimech.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.mobimech.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val mainactivityview: View = activityMainBinding.root
        setContentView(mainactivityview)


    }

}