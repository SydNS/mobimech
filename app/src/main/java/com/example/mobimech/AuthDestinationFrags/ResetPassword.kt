package com.example.mobimech.AuthDestinationFrags

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.databinding.FragmentRegistrationBinding
import com.example.mobimech.databinding.FragmentResetPasswordBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnCompleteListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPassword : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var mAuth: FirebaseAuth? = null
    lateinit var registrationBinding: FragmentRegistrationBinding
    private lateinit var emailtext: String
    private var firebaseAuthListner: FirebaseAuth.AuthStateListener? = null
    private lateinit var firbasedatabase: FirebaseDatabase

    lateinit var resetPasswordBinding: FragmentResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mAuth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        resetPasswordBinding = FragmentResetPasswordBinding.inflate(inflater)
        return resetPasswordBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetPasswordBinding.btnresetpass.setOnClickListener {
            var email: String = resetPasswordBinding.useremailreset.editText?.text.toString().trim()
            resetPassword(registrationBinding.root, email)
        }

        resetPasswordBinding.loginlinkresetpass.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_resetPassword_to_loginFrag)
        }
        resetPasswordBinding.registerlinkresetpass.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_resetPassword_to_registrationFrag)
        }
    }

    fun resetPassword(v: View, em: String) {

        when {
            TextUtils.isEmpty(em) -> {
                Toast.makeText(activity, "Enter your email!", Toast.LENGTH_SHORT).show();
            }

            em.isValidEmail()-> Toast.makeText(activity, "Enter a Valid email!", Toast.LENGTH_SHORT).show();
            else -> mAuth?.sendPasswordResetEmail(em)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Navigation.findNavController(v).navigate(R.id.action_resetPassword_to_loginFrag)

                    Log.d(TAG, "Email sent.")
                }
            }
        }
    }

    private fun String.isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResetPassword.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPassword().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}