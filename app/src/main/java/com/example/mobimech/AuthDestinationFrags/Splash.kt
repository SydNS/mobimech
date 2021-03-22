@file:Suppress("DEPRECATION")

package com.example.mobimech.AuthDestinationFrags

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.databinding.FragmentSplashBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Splash.newInstance] factory method to
 * create an instance of this fragment.
 */
class Splash : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var splashBinding: FragmentSplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        splashBinding = FragmentSplashBinding.inflate(inflater, container, false)
        return splashBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Navigation.findNavController(view).navigate(
//            R.id.action_splash_to_loginFrag
//            ,
//            null,
//            NavOptions.Builder()
//                .setPopUpTo(
//                    R.id.splash,
//                    true
//                ).build()
//        )
//        loadSplashScreen(view)

    }

    private fun loadSplashScreen(view: View) {
        Handler().postDelayed({
            // You can declare your desire activity here to open after finishing splash screen. Like MainActivity

            Navigation.findNavController(view).navigate(
                R.id.action_splash_to_walkthrough,
                null,
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.splash,
                        true
                    ).build()
            )


        }, 2000)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Splash.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Splash().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}