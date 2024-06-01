package com.example.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FragmentRec : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_rec, container, false)
        mapView = rootView.findViewById(R.id.rec_map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        googleMap.setOnMarkerClickListener { marker ->
            val festivalId = "ptyrL2HdiSd8KWfSGSss" // 파이어베이스 문서의 ID
            val intent = Intent(requireContext(), ReadBoardActivity::class.java)
            intent.putExtra("festivalId", festivalId)
            intent.putExtra("imageResId", R.drawable.samyook)
            startActivity(intent)
            true
        }
    }


    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    googleMap.addCircle(
                        CircleOptions()
                            .center(currentLatLng)
                            .radius(700.0)
                            .strokeWidth(2f)
                            .strokeColor(0x5500FF00)
                            .fillColor(0x2200FF00)
                    )
                    val fes1 = LatLng(37.643334, 127.105853)
                    val markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.markericon)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(fes1)
                            .title("학교행사")
                            .icon(BitmapDescriptorFactory.defaultMarker(240f))
                            .icon(markerIcon)
                    )
                    googleMap.uiSettings.isZoomControlsEnabled = true
                } ?: run {
                    Toast.makeText(requireContext(), "현재 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
