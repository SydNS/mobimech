@file:Suppress("DEPRECATION")

package com.example.mobimech.UI

import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.example.mobimech.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener



class UserMapUi : AppCompatActivity(), OnMapReadyCallback,
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
//    val geoQuery: GeoQuery? = null

    private var currentLogOutCustomerStatus = false
    var radius = 1.0
    var driverFound = false
    var requestType = false
    var driver_found_id: String? = null
    var DriversRef: DatabaseReference? = null
    var DriverMarker: Marker? = null
    var PickUpMarker: Marker? = null
    var DriverLocationRefListener: ValueEventListener? = null


    //    private var logoutbtn: Button? = null
//    private var profilePic: CircleImageView? = null
    private var relativeLayout: RelativeLayout? = null
    private var mAuth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_map_ui)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap.isMyLocationEnabled=true

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onConnected(p0: Bundle?) {

        locationRequest= LocationRequest()
        locationRequest.interval=1000
        locationRequest.fastestInterval=1000
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
        lastLocation=location
        val latlong = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.addMarker(
            MarkerOptions().position(latlong)
                .title("User Location")
        )
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17F))

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
}