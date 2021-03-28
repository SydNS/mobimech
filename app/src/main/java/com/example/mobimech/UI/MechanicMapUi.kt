@file:Suppress("DEPRECATION")

package com.example.mobimech.UI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.example.mobimech.R
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
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
import com.google.firebase.database.*


//Mechanic's Map Activity

class MechanicMapUi : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var googleApiClient: GoogleApiClient
    lateinit var location: Location
    private lateinit var lastLocation: Location
    lateinit var locationRequest: LocationRequest

    //getting request customer's id
    private var customerID = ""
    private var customerPickupLocation: LatLng? = null

    var driverLocationref: DatabaseReference? = null
    private lateinit var firbasedatabase: FirebaseDatabase
//    val geoQuery: GeoQuery? = null

    private var currentLogOutCustomerStatus = false
    var radius = 1.0
    var driverFound = false
    var requestType = false
    var mechanic_found_id: String? = null
    var customerSessionId: String? = null
    var MechanicRef: DatabaseReference? = null
    var DriverMarker: Marker? = null
    var PickUpMarker: Marker? = null
    var DriverLocationRefListener: ValueEventListener? = null


    //    private var logoutbtn: Button? = null
//    private var profilePic: CircleImageView? = null
    private var relativeLayout: RelativeLayout? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var logoutbtncustomer: Button
    lateinit var accepting_btn_request: Button


    private var AssignedCustomerRef: DatabaseReference? = null
    private var AssignedCustomerPickUpRef: DatabaseReference? = null

//    var PickUpMarker: Marker? = null
    private var AssignedCustomerPickUpRefListner: ValueEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mecanics_map_ui)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mAuth = FirebaseAuth.getInstance()
        firbasedatabase =
            FirebaseDatabase.getInstance("https://mobimech-d46d0-default-rtdb.firebaseio.com")
        logoutbtncustomer = findViewById(R.id.logoutbtncustomer)
        accepting_btn_request = findViewById(R.id.accepting_btn_request)
        logoutbtncustomer.setOnClickListener {
            mAuth?.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        getAssignedCustomersRequest()
    }

    private fun getAssignedCustomersRequest() {
        mechanic_found_id=mAuth?.uid

        AssignedCustomerRef = mechanic_found_id?.let {
            firbasedatabase.reference.child("Users")
                .child("Clients").child(it).child("CustomerAssignedID")
        }
        AssignedCustomerPickUpRefListner =
            AssignedCustomerRef?.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        customerID = p0.value.toString()
                        //getting assigned customer location
                        GetAssignedCustomerPickupLocation()
//                        relativeLayout!!.visibility = View.VISIBLE
                        //assignedCustomerInformation
                    } else {
                        customerID = ""
                        PickUpMarker?.remove()
                        if (AssignedCustomerPickUpRefListner != null) {
                            AssignedCustomerPickUpRef?.removeEventListener(
                                AssignedCustomerPickUpRefListner!!
                            )
                        }
//                        relativeLayout!!.visibility = View.GONE
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun GetAssignedCustomerPickupLocation() {
        AssignedCustomerPickUpRef =
            FirebaseDatabase.getInstance().reference.child("UserRequest")
                .child(customerID).child("l")
        AssignedCustomerPickUpRefListner =
            AssignedCustomerPickUpRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val customerLocationMap = dataSnapshot.getValue() as List<Any?>
                        var LocationLat = 0.0
                        var LocationLng = 0.0
                        if (customerLocationMap[0] != null) {
                            LocationLat = customerLocationMap[0].toString().toDouble()
                        }
                        if (customerLocationMap[1] != null) {
                            LocationLng = customerLocationMap[1].toString().toDouble()
                        }
                        val DriverLatLng = LatLng(LocationLat, LocationLng)
                        PickUpMarker = mMap.addMarker(
                            MarkerOptions().position(DriverLatLng).title("Customer PickUp Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logog_foreground))
                        )
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        buildGoogleApiClient()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onConnected(p0: Bundle?) {

        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            return
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(location: Location) {
        lastLocation = location
        val latlong = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.addMarker(
            MarkerOptions().position(latlong)
                .title("Mechanic Location")
        )
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17F))

//        here we record the mechanics location in the DB
        val userID: String? = FirebaseAuth.getInstance().currentUser?.uid

        val MechanicsAvailabilityRefInTheDb: DatabaseReference =
            firbasedatabase.reference.child("MechanicAvailable")

        val geoFireMechanicAvailability = GeoFire(MechanicsAvailabilityRefInTheDb)
        geoFireMechanicAvailability.setLocation(
            userID,
            GeoLocation(location.latitude, location.longitude)
        )


    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
    }


    override fun onStop() {
        super.onStop()

//        when the user gets out of the activity we remove him fro this DB

//        here we record the mechanics location in the DB
        val userID: String? = FirebaseAuth.getInstance().currentUser?.uid

        val MechanicsAvailabilityRefInTheDb: DatabaseReference =
            firbasedatabase.reference.child("MechanicAvailable")

        val geoFireMechanicAvailability = GeoFire(MechanicsAvailabilityRefInTheDb)
        geoFireMechanicAvailability.removeLocation(userID)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        onStop()
        this.finish()

    }

}


