package com.example.mobimech.homeui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.UI.MechanicMapUi
import com.example.mobimech.UI.UserMapUi
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
    ): View {
        makeorderBinding= MakeorderBinding.inflate(inflater)

        makeorderBinding.makeorders.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.)
//            startActivity(Intent(activity,UserMapUi::class.java))
            startActivity(Intent(activity,MechanicMapUi::class.java))
//            activity?.finish()

        }

        return makeorderBinding.root}

}
