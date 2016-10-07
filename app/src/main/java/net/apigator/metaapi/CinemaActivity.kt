package net.apigator.metaapi

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CinemaActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mResponseNode: ResponseNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema)

        title = "Cinemas around you"

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mResponseNode = intent.extras.getParcelable<ResponseNode>("response")
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

        mResponseNode?.children?.forEach {
            val geocoder = Geocoder(applicationContext)
            val locations = geocoder.getFromLocationName(it.node.address, 1)
            if(locations != null) {
                val position = LatLng(locations[0].latitude, locations[0].longitude)
                mMap!!.addMarker(MarkerOptions().position(position).title(it.node.name).snippet(it.node.address))
            }
        }

        // Add a marker in Sydney and move the camera
        val myPosition = LatLng(48.855800, 2.358570)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13f))
        mMap!!.setOnInfoWindowClickListener {
            val intent = Intent(applicationContext, MovieListActivity::class.java)
            intent.putExtra("cinema", mResponseNode?.children!![it.id.substring(1).toInt()])
            startActivity(intent)
        }
    }
}
