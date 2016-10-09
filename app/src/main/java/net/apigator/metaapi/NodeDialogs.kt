package net.apigator.metaapi

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.node_geo.view.*

class GeoDialog : DialogFragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("Geo", "connection failed")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d("Geo", "connection suspended")
    }

    override fun onConnected(p0: Bundle?) {
        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        dialogView?.editLatitude?.setText(location?.latitude.toString())
        dialogView?.editLongitude?.setText(location?.latitude.toString())
    }

    interface GeoDialogListener {
        fun onGeoNodeCreated(geo: SMObject)
    }

    var callback: GeoDialogListener? = null
    var dialogView: View? = null

    private var googleApiClient: GoogleApiClient? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            callback = activity as GeoDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement GeoDialogListener")
        }

        dialogView = activity.layoutInflater.inflate(R.layout.node_geo, null)

        googleApiClient = GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialogView?.buttonPosition?.setOnClickListener {
            v ->
            run {
                val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation(dialogView)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 42)
                }
            }
        }

        val builder = AlertDialog.Builder(activity)

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                .setPositiveButton("Valider") { dialog, id -> constructGeoObject(dialogView?.editLatitude?.text.toString(), dialogView?.editLongitude?.text.toString()) }
                .setNegativeButton("Annuler") { dialog, id -> dismiss() }
        return builder.create()
    }

    private fun getCurrentLocation(view: View?) {
        /*val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.PROV, 5000, 10f, locationListener(dialogView, locationManager))
        var lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(lastKnownLocation == null || lastKnownLocation.latitude == 0.0) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            dialogView?.editLatitude?.setText(lastKnownLocation?.latitude.toString())
            dialogView?.editLongitude?.setText(lastKnownLocation?.longitude.toString())
        }*/
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 42 && grantResults.size > 0 && grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation(dialogView)
        }
    }

    private fun locationListener(view: View?, locationManager: LocationManager): LocationListener {
        return object : LocationListener {
            override fun onProviderDisabled(provider: String?) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onLocationChanged(location: Location?) {
                view?.editLatitude?.setText(location?.latitude.toString())
                view?.editLongitude?.setText(location?.longitude.toString())
                locationManager.removeUpdates(this)
            }

        }
    }

    override fun onStart() {
        googleApiClient?.connect()
        super.onStart()
    }

    override fun onStop() {
        googleApiClient?.disconnect()
        super.onStop()
    }

    private fun constructGeoObject(latitude: String, longitude: String) {
        val geo = SMObject("SMGeo", SMParams(lat = latitude.toFloat(), lng = longitude.toFloat()))
        callback?.onGeoNodeCreated(geo)
    }

}

class PlaceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

}