package net.apigator.metaapi

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.node_geo.view.*

class GeoDialog : DialogFragment() {

    interface GeoDialogListener {
        fun onGeoNodeCreated(geo: SMObject)
    }

    var callback: GeoDialogListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            callback = activity as GeoDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement GeoDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity.layoutInflater.inflate(R.layout.node_geo, null)

        view.buttonPosition.setOnClickListener {
            v ->
            run {
                view.editLatitude.setText("48.855800")
                view.editLongitude.setText("2.358570")
            }
        }

        val builder = AlertDialog.Builder(activity)

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setPositiveButton("Valider") { dialog, id -> constructGeoObject(view.editLatitude.text.toString(), view.editLongitude.text.toString()) }
                .setNegativeButton("Annuler") { dialog, id -> dismiss() }
        return builder.create()
    }

    private fun constructGeoObject(latitude: String, longitude: String) {
        val geo = SMObject("Geo", SMParams(lat = latitude.toFloat(), lng = longitude.toFloat()))
        callback?.onGeoNodeCreated(geo)
    }

}

class PlaceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

}