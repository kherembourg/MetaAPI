package net.apigator.metaapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieListActivity : AppCompatActivity() {

    private var mResponseNode: ResponseNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        mResponseNode = intent.extras.getParcelable<ResponseNode>("cinema")

        title = "Movies in " + mResponseNode?.node?.name

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = MovieListAdapter(applicationContext, mResponseNode?.children ?: listOf())
    }
}
