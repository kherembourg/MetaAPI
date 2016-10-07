package net.apigator.metaapi

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class SMObject(val name: String,
                    var params: SMParams? = null,
                    var children: List<SMObject>? = null,
                    var filters: List<SMFilters>? = null)

data class SMParams(val lat: Float, val lng: Float)

data class SMFilters(val name: String, val value: String, val comparator: String)

data class Result(var result: ResponseNode?)

data class ResponseNode(var node: Node, var children: List<ResponseNode>?) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ResponseNode> = object : Parcelable.Creator<ResponseNode> {
            override fun createFromParcel(source: Parcel): ResponseNode = ResponseNode(source)
            override fun newArray(size: Int): Array<ResponseNode?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readParcelable<Node>(Node::class.java.classLoader), source.createTypedArrayList(ResponseNode.CREATOR))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(node, 0)
        dest?.writeTypedList(children)
    }
}

data class Node(var nodeName: String, var lat: Double, var lng: Double, var apiSource: String?, var overview: String?,
                var original_title: String?, var backdrop: String?, var genres: List<String>?, var popularity: Double, var rating: String?,
                var showtimes: List<String>?, var title: String?, var poster: String?, var address: String?, var name: String?) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Node> = object : Parcelable.Creator<Node> {
            override fun createFromParcel(source: Parcel): Node = Node(source)
            override fun newArray(size: Int): Array<Node?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readDouble(), source.readDouble(), source.readString(),
            source.readString(), source.readString(), source.readString(), ArrayList<String>().apply { source.readList(this, String::class.java.classLoader) },
            source.readDouble(), source.readString(), ArrayList<String>().apply { source.readList(this, String::class.java.classLoader) }, source.readString(),
            source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(nodeName)
        dest?.writeDouble(lat)
        dest?.writeDouble(lng)
        dest?.writeString(apiSource)
        dest?.writeString(overview)
        dest?.writeString(original_title)
        dest?.writeString(backdrop)
        dest?.writeList(genres)
        dest?.writeDouble(popularity)
        dest?.writeString(rating)
        dest?.writeList(showtimes)
        dest?.writeString(title)
        dest?.writeString(poster)
        dest?.writeString(address)
        dest?.writeString(name)
    }
}