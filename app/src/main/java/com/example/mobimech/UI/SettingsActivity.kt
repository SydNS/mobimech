@file:Suppress("DEPRECATION")

package com.example.mobimech.UI

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import java.util.*
import com.example.mobimech.R

class SettingsActivity : AppCompatActivity() {
    private var getType: String? = null
    private var profileImageView: ImageView? = null
    private var nameEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var driverCarName: EditText? = null
    private var closeButton: ImageView? = null
    private var saveButton: ImageView? = null
    private var profileChangeBtn: TextView? = null
    private var databaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var checker = ""
    private var imageUri: Uri? = null
    private var myUrl = ""
    private  var uploadTask: UploadTask? =null
    private var storageProfilePicsRef: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        getType = intent.getStringExtra("type")
        Toast.makeText(this, getType, Toast.LENGTH_SHORT).show()
        mAuth = FirebaseAuth.getInstance()
        databaseReference =
            getType?.let(FirebaseDatabase.getInstance("https://mobilemechan-default-rtdb.firebaseio.com/").reference.child("Users")::child)
        storageProfilePicsRef =
            FirebaseStorage.getInstance().reference.child("Profile Pictures")
        profileImageView = findViewById(R.id.profile_image)
        nameEditText = findViewById(R.id.name)
        phoneEditText = findViewById(R.id.phone_number)
        driverCarName = findViewById(R.id.driver_car_name)
        if (getType == "Drivers") driverCarName?.visibility = View.VISIBLE
        closeButton = findViewById(R.id.close_button)
        saveButton = findViewById(R.id.save_button)
        profileChangeBtn = findViewById(R.id.change_picture_btn)
        closeButton!!.setOnClickListener {
            if (getType == "Drivers") {
                startActivity(Intent(this@SettingsActivity, DriverMapActivity::class.java))
            } else {
                startActivity(Intent(this@SettingsActivity, CustomersMapActivity::class.java))
            }
        }
        saveButton!!.setOnClickListener {
            if (checker == "clicked") {
                validateControllers()
            } else {
                validateAndSaveOnlyInformation()
            }
        }
        profileChangeBtn?.setOnClickListener {
            checker = "clicked"
            CropImage.activity()
                .setAspectRatio(1, 1)
                .start(this@SettingsActivity)
        }
        userInformation
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)
            if (result != null) {
                imageUri = result.uri
            }
            profileImageView?.setImageURI(imageUri)
        } else {
            if (getType == "Drivers") {
                startActivity(Intent(this@SettingsActivity, DriverMapActivity::class.java))
            } else {
                startActivity(Intent(this@SettingsActivity, CustomersMapActivity::class.java))
            }
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateControllers() {
        if (TextUtils.isEmpty(nameEditText?.text.toString())) {
            Toast.makeText(this, "Please provide your name.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(phoneEditText?.text.toString())) {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show()
        } else {
            if (getType == "Drivers" && TextUtils.isEmpty(driverCarName?.text.toString())) {
                Toast.makeText(this, "Please provide your car Name.", Toast.LENGTH_SHORT).show()
            } else if (checker == "clicked") {
                uploadProfilePicture()
            }
        }
    }

    private fun uploadProfilePicture() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Settings Account Information")
        progressDialog.setMessage("Please wait, while we are settings your account information")
        progressDialog.show()
        if (imageUri != null) {
            val fileRef: StorageReference = storageProfilePicsRef!!.child((mAuth?.currentUser?.uid ?: String() ) + ".jpg")
            uploadTask  = fileRef.putFile(imageUri!!)
            uploadTask!!.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    val downloadUrl = p0.result
                    myUrl = downloadUrl.toString()
                    val userMap = HashMap<String, Any>()
                    userMap["uid"] = mAuth?.currentUser!!.uid
                    userMap["name"] = nameEditText?.text.toString()
                    userMap["phone"] = phoneEditText?.text.toString()
                    userMap["image"] = myUrl
                    if (getType == "Drivers") {
                        userMap["car"] = driverCarName?.text.toString()
                    }
                    mAuth?.currentUser?.let {
                        databaseReference?.child(it.uid)
                            ?.updateChildren(userMap)
                    }
                    progressDialog.dismiss()
                    if (getType == "Drivers") {
                        startActivity(
                            Intent(
                                this@SettingsActivity,
                                DriverMapActivity::class.java
                            )
                        )
                    } else {
                        startActivity(
                            Intent(
                                this@SettingsActivity,
                                CustomersMapActivity::class.java
                            )
                        )
                    }
                }
            }
        } else {
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndSaveOnlyInformation() {
        if (TextUtils.isEmpty(nameEditText?.text.toString())) {
            Toast.makeText(this, "Please provide your name.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(phoneEditText?.text.toString())) {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show()
        } else if (getType == "Drivers" && TextUtils.isEmpty(driverCarName?.text.toString())) {
            Toast.makeText(this, "Please provide your car Name.", Toast.LENGTH_SHORT).show()
        } else {
            val userMap = HashMap<String, Any>()
            userMap["uid"] = mAuth?.currentUser!!.uid
            userMap["name"] = nameEditText?.text.toString()
            userMap["phone"] = phoneEditText?.text.toString()
            if (getType == "Drivers") {
                userMap["car"] = driverCarName?.text.toString()
            }
            databaseReference?.child(mAuth?.currentUser!!.uid)?.updateChildren(userMap)
            if (getType == "Drivers") {
                startActivity(Intent(this@SettingsActivity, DriverMapActivity::class.java))
            } else {
                startActivity(Intent(this@SettingsActivity, CustomersMapActivity::class.java))
            }
        }
    }

    private val userInformation: Unit
        private get() {
            mAuth?.currentUser?.let {
                databaseReference?.child(it.uid)
                    ?.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                                val name: String = dataSnapshot.child("name").value.toString()
                                val phone: String = dataSnapshot.child("phone").value.toString()
                                nameEditText?.setText(name)
                                phoneEditText?.setText(phone)
                                if (getType == "Drivers") {
                                    val car: String = dataSnapshot.child("car").value.toString()
                                    driverCarName?.setText(car)
                                }
                                if (dataSnapshot.hasChild("image")) {
                                    val image: String =
                                        dataSnapshot.child("image").value.toString()
                                    Picasso.get().load(image).into(profileImageView)
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
            }
        }
}