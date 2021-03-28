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
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
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
import java.util.*
import kotlin.collections.HashMap


//Mechanic's Map Activity

class UserMapUi : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var googleApiClient: GoogleApiClient
    private lateinit var lastLocation: Location

    //    private lateinit var lastLocation: Location
    private lateinit var locationRequest: LocationRequest

    //getting request customer's id
    private var customerID = ""
    private var customerPickupLocation: LatLng? = null

    var mechanicLocationref: DatabaseReference? = null
    private lateinit var firbasedatabase: FirebaseDatabase
//    val geoQuery: GeoQuery? = null

    private var currentLogOutCustomerStatus = false
    var radius = 1.0
    var mechanicFound = false
    var requestType = false
    lateinit var mechanic_found_id:String
    var MechanicRef: DatabaseReference? = null
    var DriverMarker: Marker? = null
    var PickUpMarker: Marker? = null
    var MechanicLocationRefListener: ValueEventListener? = null


    //    private var logoutbtn: Button? = null
//    private var profilePic: CircleImageView? = null
    private var relativeLayout: RelativeLayout? = null
    private var mAuth: FirebaseAuth? = null
    lateinit var logoutbtncustomer: Button
    lateinit var customer_request: Button
    val fbdbroot="https://mobimech-d46d0-default-rtdb.firebaseio.com"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_map_ui)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        firbasedatabase =
            FirebaseDatabase.getInstance(fbdbroot)

        MechanicRef=firbasedatabase.getReference("MechanicAvailable")

        logoutbtncustomer = findViewById(R.id.logoutbtncustomer)
        customer_request = findViewById(R.id.customer_request)

        logoutbtncustomer.setOnClickListener {
            mAuth?.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        customer_request.setOnClickListener {

            val userID: String? = FirebaseAuth.getInstance().currentUser?.uid

            val CustomerRefInTheDb: DatabaseReference =
                firbasedatabase.reference.child("UserRequest")

            val CustomerAvailability = GeoFire(CustomerRefInTheDb)
            CustomerAvailability.setLocation(
                userID,
                GeoLocation(lastLocation.latitude, lastLocation.longitude)
            )

            customerPickupLocation = LatLng(lastLocation.latitude, lastLocation.longitude)

            mMap.moveCamera(CameraUpdateFactory.newLatLng(customerPickupLocation))
            mMap.addMarker(
                MarkerOptions().position(customerPickupLocation!!)
                    .title("Pick Up here")
            )
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11F))
            customer_request.run {
                GettingDriverLocation()
                text = "Getting You A Mechanic"
            }

            getClosestDriver()

        }
    }


    private fun getClosestDriver() {
        val mechanicsLocationref = firbasedatabase.reference.child("MechanicAvailable")
        val geoFire = GeoFire(mechanicsLocationref)
        val geoQuery: GeoQuery = geoFire.queryAtLocation(
            GeoLocation(
                customerPickupLocation!!.latitude,
                customerPickupLocation!!.longitude
            ), radius
        )

        geoQuery.removeAllListeners()

        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onKeyEntered(key: String, location: GeoLocation) {
                //anytime the driver is called this method will be called
                //key=driverID and the location
                if (!mechanicFound) {
                    mechanicFound = true
                    mechanic_found_id = key

                    var MechanRef:DatabaseReference= FirebaseDatabase.getInstance(fbdbroot).reference.child("Users").child("Mechanics").child(mechanic_found_id)

                    val customerId=FirebaseAuth.getInstance().currentUser?.uid
                    val mapOfUserDetails =HashMap<String, Any>()
                    when {
                        customerId != null -> mapOfUserDetails.put("customerId",customerId)
                    }
                    MechanRef.updateChildren(mapOfUserDetails)


                }

            }

            override fun onKeyExited(key: String) {}
            override fun onKeyMoved(key: String, location: GeoLocation) {}
            override fun onGeoQueryReady() {
                if (!mechanicFound) {
                    radius += 1
                    getClosestDriver()
                }
            }

            override fun onGeoQueryError(error: DatabaseError?) {}
        })


    }
    private fun GettingDriverLocation() {

//this will look for the driver around and online , if the driver accepts the request
// then, the driver working node is available and we get his location using his ID through the DataSnapshot

        mechanicLocationref= MechanicRef?.child("mechanicWorking")?.child(mechanic_found_id)?.child("l")
        mechanicLocationref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {


                    val map: List<Object> = p0.value as List<Object>
                    var locationLat = 0
                    var locationLng = 0
                    customer_request.text="Mechanic Found"

                    locationLat = (map[0].toString() as Double).toInt()
                    locationLng = (map[1].toString() as Double).toInt()

                    var latLng=LatLng(locationLat.toDouble(), locationLng.toDouble())

                    if (DriverMarker!=null){
                        DriverMarker?.remove()
                    }
                    DriverMarker=mMap.addMarker(
                        MarkerOptions().position(latLng)
                            .title("Your Mechanic")
                    )
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }



                override fun onLocationChanged(location: Location) {
        lastLocation = location
        val latlong = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.addMarker(
            MarkerOptions().position(latlong)
                .title("User Location")
        )
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12F))

//        here we removed the code for recording the users locationn becoz the user is static

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


    @Synchronized
    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()

    }

    override fun onStop() {
        super.onStop()
        this.finish()

    }

}