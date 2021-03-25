package com.example.mobimech.homeui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobimech.databinding.MakeorderBinding

class MakeOrder : Fragment() {
    lateinit var makeorderBinding: MakeorderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        makeorderBinding= MakeorderBinding.inflate(inflater)
        return makeorderBinding.root}

}
