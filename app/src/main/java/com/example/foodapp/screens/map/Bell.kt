package com.example.foodapp.screens.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.screens.Constant.Constant
import com.example.foodapp.screens.Constant.Constant.urlpostfix
import com.example.foodapp.screens.Constant.Constant.urlprefix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.net.URL

class Bell : Fragment() {

    private lateinit var map: MapView
    private var firstMarker: Marker? = null
    private var secondMarker: Marker? = null
    private var routeLine: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bell, container, false)

        map = view.findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val clearButton: ImageButton = view.findViewById(R.id.btn_clear)

        clearButton.visibility = View.GONE

        clearButton.setOnClickListener {
            firstMarker?.let { map.overlays.remove(it) }
            secondMarker?.let { map.overlays.remove(it) }
            routeLine?.let { map.overlays.remove(it) }

            firstMarker = null
            secondMarker = null
            routeLine = null

            map.invalidate()

            clearButton.visibility = View.GONE
        }


        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)

        val mapEventsReceiver = object : org.osmdroid.events.MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                when {
                    firstMarker == null -> {
                        firstMarker = Marker(map).apply {
                            position = p
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = Constant.Start
                        }
                        map.overlays.add(firstMarker)
                        clearButton.visibility = View.VISIBLE

                    }
                    secondMarker == null -> {
                        secondMarker = Marker(map).apply {
                            position = p
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = Constant.Destinattion
                        }
                        map.overlays.add(secondMarker)
                        fetchRoute(firstMarker!!.position, secondMarker!!.position)
                    }
                    else -> {
                        map.overlays.remove(firstMarker)
                        map.overlays.remove(secondMarker)
                        routeLine?.let { map.overlays.remove(it) }

                        firstMarker = null
                        secondMarker = null
                        routeLine = null

                        firstMarker = Marker(map).apply {
                            position = p
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = Constant.Start
                        }
                        map.overlays.add(firstMarker)
                        clearButton.visibility = View.VISIBLE
                    }
                }

                map.invalidate()
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean = false
        }

        val mapEventsOverlay = org.osmdroid.views.overlay.MapEventsOverlay(mapEventsReceiver)
        map.overlays.add(mapEventsOverlay)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        map.overlays.add(locationOverlay)

        locationOverlay.runOnFirstFix {
            val myLocation = locationOverlay.myLocation
            if (myLocation != null) {
                requireActivity().runOnUiThread {
                    map.controller.setZoom(18.0)
                    map.controller.animateTo(myLocation)
                }
            }
        }

        return view
    }

    private fun fetchRoute(start: GeoPoint, end: GeoPoint) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url =
                    "${urlprefix}${start.longitude},${start.latitude};${end.longitude},${end.latitude}${urlpostfix}"
                    Log.d("OSM_ROUTE", "Requesting: $url")

                val response = URL(url).readText()
                val json = JSONObject(response)
                val routes = json.getJSONArray(Constant.Routes)
                if (routes.length() > 0) {
                    val geometry = routes.getJSONObject(0).getJSONObject(Constant.Geometry)
                    val coordinates = geometry.getJSONArray(Constant.Cordinate)

                    val polyline = Polyline().apply {
                        outlinePaint.color = android.graphics.Color.BLUE
                        outlinePaint.strokeWidth = 8f
                    }

                    for (i in 0 until coordinates.length()) {
                        val coord = coordinates.getJSONArray(i)
                        val lon = coord.getDouble(0)
                        val lat = coord.getDouble(1)
                        polyline.addPoint(GeoPoint(lat, lon))
                    }

                    val distance = routes.getJSONObject(0).getDouble(Constant.Distance) / 1000

                    withContext(Dispatchers.Main) {
                        routeLine?.let { map.overlays.remove(it) }

                        routeLine = polyline
                        map.overlays.add(routeLine)

                        secondMarker?.subDescription =
                            Constant.DistancebyRoad.format(distance)

                        map.invalidate()
                    }
                }
            } catch (e: Exception) {
                Log.e("OSM_ROUTE", "Error: ${e.message}")
            }
        }
    }


    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
