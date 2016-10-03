package net.apigator.metaapi

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> sendRequest() }

        val data : String = "{\"name\":\"SMGeo\",\"params\":{\"lat\":\"48.855800\",\"lng\":\"2.358570\"}," +
                "\"children\":[{\"name\":\"SMCinema\"," +
                "\"children\":[{\"name\":\"SMMovie\",\"filters\":[{\"name\":\"genres\",\"value\":\"Action\",\"comparator\":\"in\"}]}]}]}"
    }

    private fun sendRequest() {
        val apiService = (applicationContext as App).retrofit.create(APIService::class.java)

        val filters = SMFilters("genres", "Action", "in")
        val movie = SMObject("SMMovie", filters = listOf(filters))
        val cinema = SMObject("SMCinema", children = listOf(movie))
        val params = SMParams(lat = 48.855800f, lng = 2.358570f)
        val geo = SMObject("SMGeo", params, listOf(cinema))

        apiService.postData(geo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<ResponseBody>() {
                        override fun onNext(response: ResponseBody?) {
                            val result = response?.string()
                            Timber.tag("Response").d(result)
                            textView.text = result
                        }

                        override fun onError(e: Throwable?) {
                            Timber.tag("Response").e(e, "WTF")
                        }

                        override fun onCompleted() {
                            Timber.tag("Response").d("Done")
                        }
                    })
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
