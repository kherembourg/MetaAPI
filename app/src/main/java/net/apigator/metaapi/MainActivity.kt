package net.apigator.metaapi

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.node_geo.view.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity(), GeoDialog.GeoDialogListener {

    var mGeo: SMObject? = null
    var mPlace: SMObject? = null
    var mCinema: SMObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> selectNode() }

        buttonRequest.setOnClickListener { sendRequest() }
    }

    private fun selectNode() {
        if (mGeo == null && mPlace == null) {
            displayDialog(listOf("Geo", "Place"))
        } else if (mGeo != null && mCinema == null && mPlace == null) {
            displayDialog(listOf("Place", "Cinema"))
        } else if (mCinema != null) {
            displayDialog(listOf("Movie"))
        }
    }

    private fun displayDialog(nodes: List<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose your node").setItems(nodes.toTypedArray()) { dialog, which ->
            when (nodes[which]) {
                "Geo" -> openGeoDialog()
                "Place" -> openPlaceDialog()
                "Cinema" -> onCinemaNodeCreated()
                "Movie" -> openMovieDialog()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun openMovieDialog() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show()
    }

    private fun openPlaceDialog() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show()
    }

    private fun openGeoDialog() {
        val dialog = GeoDialog()
        dialog.show(supportFragmentManager, "GeoDialog")
    }

    private fun sendRequest() {
        val apiService = (applicationContext as App).retrofit.create(APIService::class.java)

        val filters = SMFilters("genres", "Action", "in")
        val movie = SMObject("SMMovie", filters = listOf(filters))
        val cinema = SMObject("SMCinema", children = listOf(movie))
        val params = SMParams(lat = 48.855800f, lng = 2.358570f)
        val geo = SMObject("SMGeo", params, listOf(cinema))

        if (mCinema != null) {
            mCinema?.children = listOf(movie)
            mGeo!!.children = listOf(mCinema!!)
        }

        apiService.postData(mGeo!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Result>() {
                    override fun onNext(response: Result?) {
                        //val result = Gson().fromJson(response?.string(), Result::class.java)
                        Timber.tag("Response").d(response.toString())
                        val intent = Intent(applicationContext, CinemaActivity::class.java)
                        intent.putExtra("response", response?.result)
                        startActivity(intent)
                        }

                        override fun onError(e: Throwable?) {
                            Timber.tag("Response").e(e, "WTF")
                        }

                        override fun onCompleted() {
                            Timber.tag("Response").d("Done")
                        }
                    })
    }

    override fun onGeoNodeCreated(geo: SMObject) {
        mGeo = geo

        val geoView = layoutInflater.inflate(R.layout.node_geo, nodes_layout, false) as ViewGroup
        geoView.editLatitude.setText(geo.params?.lat.toString())
        geoView.editLatitude.inputType = EditorInfo.TYPE_NULL
        geoView.editLongitude.setText(geo.params?.lng.toString())
        geoView.editLongitude.inputType = EditorInfo.TYPE_NULL
        geoView.buttonPosition.visibility = View.GONE
        nodes_layout.addView(geoView)

        buttonRequest.isEnabled = true
    }

    private fun onCinemaNodeCreated() {
        mCinema = SMObject("SMCinema")

        val textView = TextView(this)
        textView.text = Html.fromHtml("<strong><SMCinema</strong><p>We'll look for cinemas</p>")
        nodes_layout.addView(textView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
