package com.ilaftalkful.mobileonthego.view.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.base.IlafBaseFragment
import com.ilaftalkful.mobileonthego.databinding.LocationMapFragmentBinding


class LocationMapFragment : IlafBaseFragment(), OnMapReadyCallback {

    val viewModel: LocationMapViewModel by viewModels()
    var lan :Double=0.0
    var lon :Double=0.0
    var location:String?=null
    var department:String?=null
    val options = MarkerOptions()
    var binding:LocationMapFragmentBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(null !=arguments){
            lan = arguments?.getDouble("lat")?:0.0
            lon = arguments?.getDouble("lon")?:0.0
            department = arguments?.getString("department")
            location = arguments?.getString("address")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding= DataBindingUtil.inflate<LocationMapFragmentBinding>(
             inflater, R.layout.location_map_fragment, container,
             false
         )
        binding?.mapView2?.onCreate(savedInstanceState)
        binding?.mapView2?.onResume() // needed to get the map to display immediately

        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding?.mapView2?.getMapAsync(this);
    }

    override fun onMapReady(googleMap: GoogleMap?) {
       // googleMap?.addMarker(options)
        val sydney = LatLng(lan, lon)
        googleMap!!.addMarker(
            MarkerOptions().position(sydney).title("Ilaf\n"+ department?:"").snippet(location?:"")
        )

        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onResume() {
        super.onResume()
        binding?.mapView2?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView2?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView2?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView2?.onLowMemory()
    }

}