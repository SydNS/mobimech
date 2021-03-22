package com.example.mobimech.AuthDestinationFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.databinding.FragmentRegistrationBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var registrationBinding: FragmentRegistrationBinding
    private lateinit var emailtext:String

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
        registrationBinding= FragmentRegistrationBinding.inflate(inflater)
        var view=registrationBinding.root
        mAuth = FirebaseAuth.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationBinding.registerbtn.setOnClickListener {
            var emailtext= registrationBinding.emaillogin.editText?.text.toString().trim()
            var usernametext= registrationBinding.usenname.editText?.text.toString().trim()
            var passtext1= registrationBinding.passlogin.editText?.text.toString().trim()
            var passtext2= registrationBinding.passlogin2.editText?.text.toString().trim()

            Toast.makeText(activity,"$emailtext $usernametext $passtext1 $passtext2",Toast.LENGTH_SHORT).show()

            if ( emailtext.isNotEmpty()|| usernametext.isNotEmpty()){
                if(passtext1==passtext2 ) {
                    Toast.makeText(activity, "$passtext1 is equal $passtext2", Toast.LENGTH_SHORT)
                        .show()
                    Navigation.findNavController(view)
                        .navigate(R.id.action_registrationFrag_to_loginFrag)
                }else{
                    registrationBinding.passlogin.isErrorEnabled=true
                    registrationBinding.passlogin2.isErrorEnabled=true

                }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}