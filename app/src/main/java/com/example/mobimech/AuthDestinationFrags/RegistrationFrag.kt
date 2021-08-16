@file:Suppress("DEPRECATION")

package com.example.mobimech.AuthDestinationFrags

import android.R.attr.name
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.mobimech.R
import com.example.mobimech.UI.LatestMessagesActivity
import com.example.mobimech.UI.RegisterActivity
import com.example.mobimech.UI.User
import com.example.mobimech.UI.UserMapUi
import com.example.mobimech.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.messaging_register.*
import java.util.*


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


    private lateinit var sharedPreferences: SharedPreferences

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
    ): View {


        sharedPreferences =
            activity?.getSharedPreferences("NotTheFirsttime", MODE_PRIVATE)!!

        // Inflate the layout for this fragment
        registrationBinding = FragmentRegistrationBinding.inflate(inflater)
        val view = registrationBinding.root
        mAuth = FirebaseAuth.getInstance()

        firbasedatabase =
            FirebaseDatabase.getInstance()
        loadingBar = ProgressDialog(activity)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registrationBinding.selectphotoImageviewRegister.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 0)
            context?.let { it1 ->
                CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setAspectRatio(1, 1)
                    .start(it1, this)
            }
        }

        registrationBinding.registerbtn.setOnClickListener {
            val emailtext = registrationBinding.emaillogin.editText?.text.toString().trim()
            val usernametext = registrationBinding.usenname.editText?.text.toString().trim()
            val passtext1 = registrationBinding.passlogin.editText?.text.toString().trim()
            val passtext2 = registrationBinding.passlogin2.editText?.text.toString().trim()

            if (passtext1 == passtext2) {
                createAccount(
                    emailtext,
                    passtext2,
                    view,
                    "Customers",
                    "Client",
                    usernametext
                )
                Navigation.findNavController(view)
                    .navigate(R.id.action_registrationFrag_to_homeFrag)
            } else {
                Toast.makeText(activity, "some fields are empty", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        registrationBinding.mechanregisterbtn.setOnClickListener {

            val emailtext = registrationBinding.emaillogin.editText?.text.toString().trim()
            val usernametext = registrationBinding.usenname.editText?.text.toString().trim()
            val passtext1 = registrationBinding.passlogin.editText?.text.toString().trim()
            val passtext2 = registrationBinding.passlogin2.editText?.text.toString().trim()

            if (passtext1 == passtext2) {
                createAccount(
                    emailtext,
                    passtext2,
                    view,
                    "Mechanics",
                    "Mechanic",
                    usernametext

                )
                Navigation.findNavController(view)
                    .navigate(R.id.action_registrationFrag_to_homeMechanic)
            } else {
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
        appuser: String,
        username: String
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
                performRegistration(username)

                Toast.makeText(activity, "Welcome $appuser", Toast.LENGTH_SHORT).show()

                loadingBar!!.dismiss()


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

    private var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == FragmentActivity.RESULT_OK && data != null) {
            val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)
            if (result != null) {
                selectedPhotoUri = result.uri
            }

            Picasso.get().load(selectedPhotoUri.toString())
                .into(registrationBinding.selectphotoImageviewRegister)
//            registrationBinding.imuri.text=selectedPhotoUri.toString()
        } else {
            Toast.makeText(activity, "No image", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//            selectedPhotoUri = data.data ?: return
//            Log.d(RegisterActivity.TAG, "Photo was selected")
//            // Get and resize profile image
//            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//            activity?.contentResolver?.query(selectedPhotoUri!!, filePathColumn, null, null, null)
//                ?.use {
//                    it.moveToFirst()
//                    val columnIndex = it.getColumnIndex(filePathColumn[0])
//                    val picturePath = it.getString(columnIndex)
//                    // If picture chosen from camera rotate by 270 degrees else
//                    if (picturePath.contains("DCIM")) {
//                        Picasso.get().load(selectedPhotoUri).rotate(270f)
//                            .into(registrationBinding.selectphotoImageviewRegister)
//                    } else {
//                        Picasso.get().load(selectedPhotoUri).into(registrationBinding.selectphotoImageviewRegister)
//                    }
//                }
//            selectphoto_button_register.alpha = 0f
//        }
//    }

    private fun performRegistration(name: String) {


        if (selectedPhotoUri == null) {
            Toast.makeText(activity, "Please select a photo", Toast.LENGTH_SHORT).show()
            return
        }
        uploadImageToFirebaseStorage(name)

    }

    private fun uploadImageToFirebaseStorage(username: String) {
        if (selectedPhotoUri == null) {
            // save user without photo
            saveUserToFirebaseDatabase(null, username)
        } else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(RegisterActivity.TAG, "Successfully uploaded image: ${it.metadata?.path}")

                    @Suppress("NestedLambdaShadowedImplicitParameter")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(RegisterActivity.TAG, "File Location: $it")
                        saveUserToFirebaseDatabase(it.toString(), username)
                    }
                }
                .addOnFailureListener {
                    Log.d(RegisterActivity.TAG, "Failed to upload image to storage: ${it.message}")
                    loading_view.visibility = View.GONE
                    already_have_account_text_view.visibility = View.VISIBLE
                }
        }

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String?, username: String) {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = if (profileImageUrl == null) {
            User(uid, username, null)
        } else {
            User(uid, username, profileImageUrl)
        }

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(RegisterActivity.TAG, "Finally we saved the user ")


            }
            .addOnFailureListener {
                Log.d(RegisterActivity.TAG, "Failed to set value to database: ${it.message}")
                loading_view.visibility = View.GONE
                already_have_account_text_view.visibility = View.VISIBLE
            }
    }
}