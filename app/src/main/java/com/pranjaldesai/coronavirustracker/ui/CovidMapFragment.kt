package com.pranjaldesai.coronavirustracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.models.CovidStats
import com.pranjaldesai.coronavirustracker.databinding.FragmentCovidMapBinding
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment

class CovidMapFragment : CoreFragment<FragmentCovidMapBinding>(), IPrimaryFragment,
    OnMapReadyCallback {
    override val layoutResourceId: Int = R.layout.fragment_covid_map

    private val bottomNavOptionId: Int = R.id.fragmentOne
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private var googleMap: GoogleMap? = null
    private var tileOverlay: TileOverlay? = null
    private val markerList = ArrayList<Marker?>()
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101
    private var currentZoomLevel = 3.0f
    var colors = intArrayOf(
        Color.rgb(240, 85, 69),
        Color.rgb(127, 0, 0)
    )

    var startPoints = floatArrayOf(
        0.5f, 1f
    )

    override fun bindData() {
        super.bindData()
        context?.let {
            if (checkLocationPermission().not()) {
                googleMap?.isMyLocationEnabled = false
                activity?.let { activity ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                googleMap?.isMyLocationEnabled = true
            }
        }

        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(CovidStats::class.java)
                tileOverlay?.clearTileCache()
                tileOverlay?.remove()

                val locationList = ArrayList<LatLng>()
                markerList.clear()
                post?.confirmed?.infectedLocations?.forEach {
                    val lat = it.coordinates?.lat?.toDouble()
                    val long = it.coordinates?.long?.toDouble()
                    if (lat != null && long != null) {
                        val coordinates = LatLng(lat, long)
                        val locationTitle =
                            if (it.infectedProvince != null && it.infectedProvince != "empty") {
                                it.infectedProvince
                            } else {
                                it.infectedCountry
                            }
                        markerList.add(
                            googleMap?.addMarker(
                                MarkerOptions().position(coordinates).title(locationTitle)
                                    .visible(false)
                            )
                        )
                        locationList.add(coordinates)
                    }
                }
                val gradient = Gradient(colors, startPoints, 50000)
                val mProvider = HeatmapTileProvider.Builder()
                    .data(locationList)
                    .gradient(gradient)
                    .build()
                mProvider.setRadius(250)
                tileOverlay =
                    googleMap?.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loge(databaseError.toException())
            }
        }
        databaseRef.addValueEventListener(postListener)
    }

    override fun onResume() {
        super.onResume()
        subscribeToNavigationHost()
        updateBottomNavigationSelection(bottomNavOptionId)
    }

    override fun onPause() {
        super.onPause()
        unsubscribeFromNavigationHost()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            googleMap?.isMyLocationEnabled = checkLocationPermission()
        }

    }

    private fun checkLocationPermission(): Boolean {
        return context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } ?: false
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.isMyLocationEnabled = checkLocationPermission()
        googleMap?.uiSettings?.isMyLocationButtonEnabled = checkLocationPermission()
        googleMap?.setOnCameraMoveListener {
            val zoomLevel = googleMap?.cameraPosition?.zoom
            if (zoomLevel != null && zoomLevel > 4.3 && currentZoomLevel != zoomLevel) {
                currentZoomLevel = zoomLevel
                markerList.forEach {
                    it?.isVisible = true
                }
                tileOverlay?.clearTileCache()
            } else if (zoomLevel != null && zoomLevel <= 4.3 && currentZoomLevel != zoomLevel) {
                currentZoomLevel = zoomLevel
                markerList.forEach {
                    it?.isVisible = false
                }
                tileOverlay?.clearTileCache()
            }
        }
    }
}