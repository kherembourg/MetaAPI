package net.apigator.metaapi

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie.view.*
import net.apigator.metaapi.R.layout.item_movie


class MovieListAdapter(val context: Context, var list: List<ResponseNode>) : RecyclerView.Adapter<MovieListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MovieListHolder {
        val view = LayoutInflater.from(context).inflate(item_movie, parent, false)
        return MovieListHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieListHolder?, position: Int) {
        holder?.bindNode(list[position])
    }

}

class MovieListHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindNode(responseNode: ResponseNode) {
        with(responseNode) {
            itemView.title.text = responseNode.node.title
            Glide.with(itemView.context).load(responseNode.node.poster).into(itemView.image)
        }
    }
}