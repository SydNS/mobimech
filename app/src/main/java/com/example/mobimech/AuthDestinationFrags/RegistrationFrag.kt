@file:Suppress("DEPRECATION")

package com.example.mobimech.AuthDestinationFrags

import android.R.attr.name
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


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


    private lateinit var sharedPreferences:SharedPreferences

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var registrationBinding: FragmentRegistrationBinding
    private lateinit var emailtext: String
    private var firebaseAuthListner: FirebaseAuth.AuthStateListener? = null
    private lateinit var firbasedatabase: FirebaseDatabase

    //    private lateinit var mechanfirbasedatabase:FirebaseDatabase
    var MechanicRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null

    var currentUserId: String? = null

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        sharedPreferences =
//            context.getSharedPreferences("NotTheFirsttime", MODE_PRIVATE)
//    }

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


        sharedPreferences =
            activity?.getSharedPreferences("NotTheFirsttime", Context.MODE_PRIVATE)!!

        // Inflate the layout for this fragment
        registrationBinding = FragmentRegistrationBinding.inflate(inflater)
        var view = registrationBinding.root
        mAuth = FirebaseAuth.getInstance()

        firbasedatabase =
            FirebaseDatabase.getInstance("https://mobilemechan-default-rtdb.firebaseio.com/")
        loadingBar = ProgressDialog(activity)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        registrationBinding.registerbtn.setOnClickListener {
            val emailtext = registrationBinding.emaillogin.editText?.text.toString().trim()
            var usernametext = registrationBinding.usenname.editText?.text.toString().trim()
            val passtext1 = registrationBinding.passlogin.editText?.text.toString().trim()
            val passtext2 = registrationBinding.passlogin2.editText?.text.toString().trim()

            if (passtext1 == passtext2) createAccount(
                emailtext,
                passtext2,
                view,
                "Customers",
                "Client"
            ) else {
                Toast.makeText(activity, "some fields are empty", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        registrationBinding.mechanregisterbtn.setOnClickListener {

            val emailtext = registrationBinding.emaillogin.editText?.text.toString().trim()
            var usernametext = registrationBinding.usenname.editText?.text.toString().trim()
            val passtext1 = registrationBinding.passlogin.editText?.text.toString().trim()
            val passtext2 = registrationBinding.passlogin2.editText?.text.toString().trim()

            if (passtext1 == passtext2) createAccount(
                emailtext,
                passtext2,
                view,
                "Mechanics",
                "Mechanic"
            ) else {
                Toast.makeText(activity, "some fields are empty", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        registrationBinding.loginlink.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registrationFrag_to_loginFrag)
        }

    }

    private fun createAccount(
        email: String,
        password: String,
        view: View,
        childname: String,
        appuser: String
    ) {
//
//        // Storing data into SharedPreferences
//        val alreadyinstalled = sharedPreferences?.edit()
//        alreadyinstalled.putString("Answer","yes")
//        alreadyinstalled.apply()


        // [START create_user_with_email]

        loadingBar!!.setTitle("Please wait :")
        loadingBar!!.setMessage("While system is performing processing on your data...")
        loadingBar!!.show()

        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {

                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = mAuth?.currentUser

                currentUserId = user?.uid
                MechanicRef = firbasedatabase
                    .reference
                    .child("Users").child(childname).child(currentUserId!!)
//                        .child("Users").child("Mechanics").child(currentUserId!!)
                MechanicRef!!.setValue(true)

                Toast.makeText(activity, "Welcome $appuser", Toast.LENGTH_SHORT).show()

                loadingBar!!.dismiss()


                Navigation.findNavController(view)
                    .navigate(R.id.action_registrationFrag_to_walkthrough)
            } else {
                // If sign in fails, display a message to the user.
//                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    activity,
                    "Error Occured ${it.exception?.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
                loadingBar!!.dismiss()

            }

        }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth?.currentUser
        firebaseAuthListner?.let { mAuth?.addAuthStateListener(it) }

    }

    override fun onStop() {
        super.onStop()
        firebaseAuthListner?.let { mAuth?.removeAuthStateListener(it) }
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