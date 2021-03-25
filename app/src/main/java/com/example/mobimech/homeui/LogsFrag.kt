package com.example.mobimech.homeui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobimech.databinding.LogsfragBinding

class LogsFrag : Fragment() {
    lateinit var logsfragBinding: LogsfragBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logsfragBinding= LogsfragBinding.inflate(inflater)
        return logsfragBinding.root
    }

}
