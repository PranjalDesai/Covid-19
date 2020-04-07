package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
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
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.databinding.FragmentCovidMapBinding
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.helper.generateCityTitle
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment
import com.pranjaldesai.coronavirustracker.ui.shared.subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class CovidMapFragment : CoreFragment<FragmentCovidMapBinding>(), IPrimaryFragment,
    OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, ICovidView {

    override val layoutResourceId: Int = R.layout.fragment_covid_map
    override val lifecycleOwner: LifecycleOwner by lazy { this }

    private var googleMap: GoogleMap? = null
    private var tileOverlay: TileOverlay? = null
    private var currentZoomLevel = DEFAULT_ZOOM

    private val viewModel: CovidMapViewModel by viewModel()
    private val bottomNavOptionId: Int = R.id.covidMap
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val markerList = ArrayList<Marker?>()
    override val toolbar: Toolbar? by lazy { binding.toolbar }
    override val toolbarTitle: String by lazy { getString(R.string.covid_map_toolbar_title) }
    override val useCustomBackButtonAction: Boolean = true
    private val heatStartPoints = floatArrayOf(0.5f, 1f)
    private val heatMapColors = intArrayOf(Color.rgb(240, 85, 69), Color.rgb(127, 0, 0))
    private var isDarkMode = false
    private var areMarkersVisible = false
    private lateinit var mapFragment: SupportMapFragment

    override fun bindData() {
        viewModel.subscribe(this, lifecycleOwner)
        super.bindData()
        isDarkMode = resources.getBoolean(R.bool.isDarkMode)
        mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(CovidStats::class.java)
                viewModel.covidStats = post
                tileOverlay?.clearTileCache()
                tileOverlay?.remove()
                markerList.clear()
                generateHeatMap(post)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loge(databaseError.toException())
            }
        }
        databaseRef.addValueEventListener(postListener)
    }

    override fun onBackButtonClicked() {
        super.onBackButtonClicked()
        activity?.startActivity(homeIntent)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        updateMapStyle()

        googleMap?.setOnInfoWindowClickListener(this)
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(DEFAULT_LAT, DEFAULT_LONG), DEFAULT_ZOOM
            )
        )
        googleMap?.setOnCameraMoveListener {
            updateMarkerZoomOnMovement()
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        val countryName = marker.tag as String?
        countryName?.let {
            val overallCountry = viewModel.generateOverallCountry(it)
            overallCountry?.let {
                navigateToCountryDetail(overallCountry)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        subscribeToNavigationHost()
        updateBottomNavigationSelection(bottomNavOptionId)
        mapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        unsubscribeFromNavigationHost()
    }

    private fun updateMapStyle() {
        if (isDarkMode) {
            googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        } else {
            googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
    }

    private fun navigateToCountryDetail(country: OverallCountry) {
        findNavController().navigate(
            CovidMapFragmentDirections.actionCovidMapToCountryDetailFragment(country)
        )
    }

    private fun generateHeatMap(post: CovidStats?) {
        post?.confirmed?.infectedLocations?.forEach {
            val coordinates = viewModel.generateCoordinates(it)
            val locationTitle = generateCityTitle(it)
            val snippet = viewModel.generateSnippet(it)
            val tag = viewModel.generateTag(it)
            if (coordinates != null && snippet != null) {
                val marker = googleMap?.addMarker(
                    MarkerOptions().position(coordinates).title(locationTitle)
                        .snippet(snippet)
                        .visible(false)
                )
                marker?.tag = tag
                markerList.add(marker)
            }
        }
        val gradient = Gradient(heatMapColors, heatStartPoints, GRADIENT_MAP_SIZE)
        val coordinates = viewModel.generateCoordinatesList()
        if (coordinates.isNotEmpty()) {
            val mProvider = HeatmapTileProvider.Builder()
                .weightedData(coordinates)
                .gradient(gradient)
                .build()
            mProvider.setRadius(HEAT_MAP_RADIUS)
            tileOverlay = googleMap?.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
        }
    }

    private fun updateMarkerZoomOnMovement() {
        val zoomLevel = googleMap?.cameraPosition?.zoom
        val showMarkers = viewModel.shouldShowMarkers(currentZoomLevel, zoomLevel)
        if (showMarkers != null && areMarkersVisible != showMarkers) {
            currentZoomLevel = zoomLevel ?: DEFAULT_ZOOM
            markerList.forEach {
                it?.isVisible = showMarkers
            }
            areMarkersVisible = showMarkers
        }
    }

    companion object {
        const val PERMISSION_REQUEST_COARSE_LOCATION = 101
        const val DEFAULT_LAT = 39.38
        const val DEFAULT_LONG = -97.92
        const val DEFAULT_ZOOM = 3.0F
        const val DEFAULT_MARKER_SNIPPET = "Infected:"
        const val HEAT_MAP_RADIUS = 275
        const val GRADIENT_MAP_SIZE = 50
    }
}