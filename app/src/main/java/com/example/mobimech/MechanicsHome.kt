package com.example.mobimech

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mobimech.UI.MechanicMapUi
import com.example.mobimech.databinding.FragmentMechanicsHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MechanicsHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class MechanicsHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mechanicsHomeBinding: FragmentMechanicsHomeBinding

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
        mechanicsHomeBinding= FragmentMechanicsHomeBinding.inflate(layoutInflater)

        val v=mechanicsHomeBinding.root

        v.setOnClickListener {
//            Navigation.findNavController(v).navigate(R.id.mechanicMapUi)
//            stopped here for monday to move to the debrief
//            ActivityNavigator(requireActivity()) s
//                .createDestination()
//                .setIntent(Intent(requireActivity(), MechanicMapUi::class.java))
//                .navigate(null, null)
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MechanicsHome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MechanicsHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}