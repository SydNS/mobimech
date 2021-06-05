@file:Suppress("DEPRECATION")

package com.example.mobimech.UI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.mobimech.R
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

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
import kotlin.collections.HashMap


//Mechanic's Map Activity

class UserMapUi : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private var mMap: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null
    var LastLocation: Location? = null
    var locationRequest: LocationRequest? = null
    private var Logout: Button? = null
    private var SettingsButton: Button? = null
    private var CallCabCarButton: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var CustomerDatabaseRef: DatabaseReference? = null
    private var CustomerPickUpLocation: LatLng? = null
    private var DriverAvailableRef: DatabaseReference? = null
    private var DriverLocationRef: DatabaseReference? = null
    private var DriversRef: DatabaseReference? = null
    private var radius = 1
    private var driverFound: Boolean? = false
    private var requestType = false
    private var driverFoundID: String? = null
    private var customerID: String? = null
    var DriverMarker: Marker? = null
    var PickUpMarker: Marker? = null
    var geoQuery: GeoQuery? = null
    private var DriverLocationRefListner: ValueEventListener? = null
    private var txtName: TextView? = null
    private var txtPhone: TextView? = null
    private var txtCarName: TextView? = null
    private var profilePic: ImageView? = null
    private var relativeLayout: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_map_ui)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        customerID = FirebaseAuth.getInstance().getCurrentUser().uid
        CustomerDatabaseRef = FirebaseDatabase.getInstance("https://mobilemechan-default-rtdb.firebaseio.com/").reference.child("Customer Requests")
        DriverAvailableRef = FirebaseDatabase.getInstance("https://mobilemechan-default-rtdb.firebaseio.com/").reference.child("Mechanics Available")
        DriverLocationRef = FirebaseDatabase.getInstance("https://mobilemechan-default-rtdb.firebaseio.com/").reference.child("Mechanics Working")
        Logout = findViewById<Button>(R.id.logout_customer_btn)
        SettingsButton = findViewById<Button>(R.id.settings_customer_btn)
        CallCabCarButton = findViewById<Button>(R.id.call_a_mechanic_button)
        txtName = findViewById(R.id.name_driver)
        txtPhone = findViewById(R.id.phone_driver)
        txtCarName = findViewById(R.id.car_name_driver)
        profilePic = findViewById(R.id.profile_image_driver)
        relativeLayout = findViewById(R.id.rel1)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment: SupportMapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        SettingsButton!!.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("type", "Customers")
            startActivity(intent)
        }
        Logout!!.setOnClickListener {
            mAuth!!.signOut()
            LogOutUser()
        }
        CallCabCarButton!!.setOnClickListener {
            if (requestType) {
                requestType = false
                geoQuery?.removeAllListeners()
                DriverLocationRefListner?.let { it1 -> DriverLocationRef?.removeEventListener(it1) }
                if (driverFound != null) {
                    DriversRef = driverFoundID?.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Users").child("Mechanics").child(it1).child("CustomerRideID")
                    }
                    DriversRef?.removeValue()
                    driverFoundID = null
                }
                driverFound = false
                radius = 1
                val customerId: String = FirebaseAuth.getInstance().getCurrentUser().uid
                val geoFire = GeoFire(CustomerDatabaseRef)
                geoFire.removeLocation(customerId)
                PickUpMarker?.remove()
                DriverMarker?.remove()
                CallCabCarButton!!.text = "Call a Mechanic"
                relativeLayout?.visibility = View.GONE
            } else {
                requestType = true
                val customerId: String = FirebaseAuth.getInstance().getCurrentUser().getUid()
                val geoFire = GeoFire(CustomerDatabaseRef)
                geoFire.setLocation(customerId, GeoLocation(LastLocation!!.latitude, LastLocation!!.longitude))
                CustomerPickUpLocation = LatLng(LastLocation!!.latitude, LastLocation!!.longitude)
                PickUpMarker = mMap?.addMarker(MarkerOptions().position(CustomerPickUpLocation!!).title("My Location").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.user)))
                CallCabCarButton!!.text = "Getting your Mechanic..."
                closetDriverCab
            }
        }
    }//we tell driver which customer he is going to have

    //Show driver location on customerMapActivity
    //anytime the driver is called this method will be called
    //key=driverID and the location
    private val closetDriverCab: Unit
        get() {
            val geoFire = GeoFire(DriverAvailableRef)
            geoQuery = geoFire.queryAtLocation(CustomerPickUpLocation?.let { GeoLocation(it.latitude, CustomerPickUpLocation!!.longitude) },
                radius.toDouble()
            )
            geoQuery?.removeAllListeners()
            geoQuery?.addGeoQueryEventListener(object : GeoQueryEventListener {
                override fun onKeyEntered(key: String?, location: GeoLocation?) {
                    //anytime the driver is called this method will be called
                    //key=driverID and the location
                    if (!driverFound!! && requestType) {
                        driverFound = true
                        driverFoundID = key


                        //we tell driver which customer he is going to have
                        DriversRef =
                            driverFoundID?.let {
                                FirebaseDatabase.getInstance().reference.child("Users").child("Mechanics").child(
                                    it
                                )
                            }
                        val driversMap = HashMap<String, Any?>()
                        driversMap["CustomerRideID"] = customerID
                        DriversRef!!.updateChildren(driversMap)

                        //Show driver location on customerMapActivity
                        GettingDriverLocation()
                        CallCabCarButton!!.text = "Looking for Mechanics Location..."
                    }
                }

                override fun onKeyExited(key: String?) {}
                override fun onKeyMoved(key: String?, location: GeoLocation?) {}
                override fun onGeoQueryReady() {
                    if (!driverFound!!) {
                        radius += 1
                        closetDriverCab
                    }
                }

                override fun onGeoQueryError(error: DatabaseError?) {}
            })
        }

    //and then we get to the driver location - to tell customer where is the driver
    private fun GettingDriverLocation() {
        DriverLocationRefListner = driverFoundID?.let {
            DriverLocationRef?.child(it)?.child("l")
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists() && requestType) {
                            val driverLocationMap = dataSnapshot.value as List<Any?>
                            var LocationLat = 0.0
                            var LocationLng = 0.0
                            CallCabCarButton!!.text = "Mechanic Found"
                            relativeLayout?.visibility = View.VISIBLE
                            assignedDriverInformation
                            if (driverLocationMap[0] != null) {
                                LocationLat = driverLocationMap[0].toString().toDouble()
                            }
                            if (driverLocationMap[1] != null) {
                                LocationLng = driverLocationMap[1].toString().toDouble()
                            }

                            //adding marker - to pointing where driver is - using this lat lng
                            val DriverLatLng = LatLng(LocationLat, LocationLng)
                            DriverMarker?.remove()
                            val location1 = Location("")
                            location1.latitude = CustomerPickUpLocation!!.latitude
                            location1.longitude = CustomerPickUpLocation!!.longitude
                            val location2 = Location("")
                            location2.latitude = DriverLatLng.latitude
                            location2.longitude = DriverLatLng.longitude
                            val Distance = location1.distanceTo(location2)
                            if (Distance < 90) {
                                CallCabCarButton!!.text = "Mechanic's Reached"
                            } else {
                                CallCabCarButton!!.text = "Meachanic Found: $Distance"
                            }
                            DriverMarker = mMap?.addMarker(MarkerOptions().position(DriverLatLng).title("your mechanic is here").icon(BitmapDescriptorFactory.fromResource(R.drawable.car)))
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        // now let set user location enable
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            return
        }
        buildGoogleApiClient()
        mMap?.isMyLocationEnabled = true
    }

    override fun onConnected(@Nullable bundle: Bundle?) {
        locationRequest = LocationRequest()
        locationRequest?.setInterval(1000)
        locationRequest?.setFastestInterval(1000)
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            //
            return
        }
        //it will handle the refreshment of the location
        //if we dont call it we will get location only once
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this
        )
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(@NonNull connectionResult: ConnectionResult) {}
    override fun onLocationChanged(location: Location) {
        //getting the updated location
        LastLocation = location
        val latLng = LatLng(location.latitude, location.longitude)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(12F))
    }

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
    }

    fun LogOutUser() {
        val startPageIntent = Intent(this, MainActivity::class.java)
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(startPageIntent)
        finish()
    }

    private val assignedDriverInformation: Unit
        private get() {
            val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
                .child("Users").child("Drivers").child(driverFoundID!!)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        val name: String = dataSnapshot.child("name").value.toString()
                        val phone: String = dataSnapshot.child("phone").value.toString()
                        val car: String = dataSnapshot.child("car").value.toString()
                        txtName?.text = name
                        txtPhone?.text = phone
                        txtCarName?.text = car
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