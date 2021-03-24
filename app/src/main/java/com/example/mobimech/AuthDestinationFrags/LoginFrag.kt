package com.example.mobimech.AuthDestinationFrags

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var loginBinding: FragmentLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBinding.loginbtn.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_homeFrag)

            val email:String=loginBinding.emaillogin.editText.toString().trim()
            val password:String=loginBinding.passlogin.editText.toString()

            if(email.isEmpty()){
                Toast.makeText(activity,"Fill in all the Fields",Toast.LENGTH_SHORT).show()
            }
            if(password.isEmpty()){
                Toast.makeText(activity,"Fill in all the Fields",Toast.LENGTH_SHORT).show()
            }
            signIn(email,password,view)

            Toast.makeText(
                activity,
                "Welcome home",
                Toast.LENGTH_LONG
            ).show()
        }

        loginBinding.registerlink.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_registrationFrag)
            Toast.makeText(
                activity,
                "Register for an account",
                Toast.LENGTH_LONG
            ).show()


        }
        loginBinding.resetpasswordlink.setOnClickListener {
            Toast.makeText(
                activity,
                "Reset Your Password",
                Toast.LENGTH_LONG
            ).show()
            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_resetPassword)

        }


    }


    private fun signIn(email: String, password: String,view: View) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth?.currentUser
                Toast.makeText(activity,user.toString(),Toast.LENGTH_SHORT).show()

                Navigation.findNavController(view).navigate(
                    R.id.action_loginFrag_to_homeFrag,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(
                            R.id.splash,
                            true
                        ).build()
                )


            }else{

                Toast.makeText(activity,"Sorry failed to login ${it.exception}",Toast.LENGTH_SHORT).show()


            }
        }

        // [END sign_in_with_email]
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}