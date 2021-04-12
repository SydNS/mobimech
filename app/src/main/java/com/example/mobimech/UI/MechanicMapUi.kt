@file:Suppress("DEPRECATION")

package com.example.mobimech.UI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.*
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.mobimech.R
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationListener

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


//Mechanic's Map Activity

open class MechanicMapUi : FragmentActivity(), OnMapReadyCallback,
GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
LocationListener {
    private var mMap: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null
    var LastLocation: Location? = null
    var locationRequest: LocationRequest? = null
    private var LogoutDriverBtn: Button? = null
    private var SettingsDriverButton: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var currentLogOutUserStatus = false

    //getting request customer's id
    private var customerID = ""
    private var driverID: String? = null
    private var AssignedCustomerRef: DatabaseReference? = null
    private var AssignedCustomerPickUpRef: DatabaseReference? = null
    var PickUpMarker: Marker? = null
    private var AssignedCustomerPickUpRefListner: ValueEventListener? = null
    private var txtName: TextView? = null
    private var txtPhone: TextView? = null
    private var profilePic: CircleImageView? = null
    private var relativeLayout: RelativeLayout? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //notice
        setContentView(R.layout.activity_mecanics_map_ui)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        driverID = mAuth!!.currentUser.uid
        LogoutDriverBtn = findViewById<Button>(R.id.logout_driv_btn)
        SettingsDriverButton = findViewById<Button>(R.id.settings_driver_btn)
        txtName = findViewById(R.id.name_customer)
        txtPhone = findViewById(R.id.phone_customer)
        profilePic = findViewById(R.id.profile_image_customer)
        relativeLayout = findViewById(R.id.rel2)
        val mapFragment: SupportMapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        SettingsDriverButton!!.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("type", "Drivers")
            startActivity(intent)
        }
        LogoutDriverBtn!!.setOnClickListener {
            currentLogOutUserStatus = true
            DisconnectDriver()
            mAuth!!.signOut()
            LogOutUser()
        }
        assignedCustomersRequest
    }

    //getting assigned customer location
    private val assignedCustomersRequest: Unit
    private get() {
        AssignedCustomerRef = driverID?.let {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child("Drivers").child(it).child("CustomerRideID")
        }
        AssignedCustomerRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    customerID = dataSnapshot.value.toString()
                    //getting assigned customer location
                    GetAssignedCustomerPickupLocation()
                    relativeLayout?.visibility = View.VISIBLE
                    assignedCustomerInformation
                } else {
                    customerID = ""
                    PickUpMarker?.remove()
                    if (AssignedCustomerPickUpRefListner != null) {
                        AssignedCustomerPickUpRef?.removeEventListener(
                            AssignedCustomerPickUpRefListner!!
                        )
                    }
                    relativeLayout?.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun GetAssignedCustomerPickupLocation() {
        AssignedCustomerPickUpRef =
            FirebaseDatabase.getInstance().reference.child("Customer Requests")
                .child(customerID).child("l")
        AssignedCustomerPickUpRefListner =
            AssignedCustomerPickUpRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val customerLocationMap = dataSnapshot.value as List<Any?>
                        var LocationLat = 0.0
                        var LocationLng = 0.0
                        if (customerLocationMap[0] != null) {
                            LocationLat = customerLocationMap[0].toString().toDouble()
                        }
                        if (customerLocationMap[1] != null) {
                            LocationLng = customerLocationMap[1].toString().toDouble()
                        }
                        val DriverLatLng = LatLng(LocationLat, LocationLng)
                        PickUpMarker = mMap?.addMarker(
                            MarkerOptions().position(DriverLatLng).title("Customer PickUp Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // now let set user location enable
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PERMISSION_GRANTED
        ) {
            return
        }
        buildGoogleApiClient()
        mMap!!.isMyLocationEnabled = true
    }

    override fun onCreate(
        @Nullable savedInstanceState: Bundle?,
        @Nullable persistentState: PersistableBundle?
    ) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onLocationChanged(location: Location) {
        if (applicationContext != null) {
            //getting the updated location
            LastLocation = location
            val latLng = LatLng(location.latitude, location.longitude)
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap?.animateCamera(CameraUpdateFactory.zoomTo(12f))
            val userID: String = FirebaseAuth.getInstance().currentUser.uid
            val DriversAvailabilityRef: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("Drivers Available")
            val geoFireAvailability = GeoFire(DriversAvailabilityRef)
            val DriversWorkingRef: DatabaseReference =
                FirebaseDatabase.getInstance().reference.child("Drivers Working")
            val geoFireWorking = GeoFire(DriversWorkingRef)
            when (customerID) {
                "" -> {
                    geoFireWorking.removeLocation(userID)
                    geoFireAvailability.setLocation(
                        userID,
                        GeoLocation(location.latitude, location.longitude)
                    )
                }
                else -> {
                    geoFireAvailability.removeLocation(userID)
                    geoFireWorking.setLocation(
                        userID,
                        GeoLocation(location.latitude, location.longitude)
                    )
                }
            }
        }
    }

    override fun onConnected(@Nullable bundle: Bundle?) {
        locationRequest = LocationRequest()
        locationRequest!!.interval = 1000
        locationRequest!!.fastestInterval = 1000
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            !== PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PERMISSION_GRANTED
        ) {
            //
            return
        }
        //it will handle the refreshment of the location
        //if we dont call it we will get location only once
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this)
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(@NonNull connectionResult: ConnectionResult) {}

    //create this method -- for useing apis
    @Synchronized
    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient?.connect()
    }

    protected override fun onStop() {
        super.onStop()
        if (!currentLogOutUserStatus) {
            DisconnectDriver()
        }
    }

    private fun DisconnectDriver() {
        val userID: String = FirebaseAuth.getInstance().getCurrentUser().uid
        val DriversAvailabiltyRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Drivers Available")
        val geoFire = GeoFire(DriversAvailabiltyRef)
        geoFire.removeLocation(userID)
    }

    fun LogOutUser() {
        val startPageIntent = Intent(this, MainActivity::class.java)
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(startPageIntent)
        finish()
    }

    private val assignedCustomerInformation: Unit
    private get() {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
            .child("Users").child("Customers").child(customerID)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                    val name: String = dataSnapshot.child("name").value.toString()
                    val phone: String = dataSnapshot.child("phone").value.toString()
                    txtName?.text = name
                    txtPhone?.text = phone
                    if (dataSnapshot.hasChild("image")) {
                        val image: String = dataSnapshot.child("image").value.toString()
                        Picasso.get().load(image).into(profilePic)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}