package net.apigator.metaapi

data class SMObject(val name: String,
                    var params: SMParams? = null,
                    var children: List<SMObject>? = null,
                    var filters: List<SMFilters>? = null)

data class SMParams(val lat: Float, val lng: Float)

data class SMFilters(val name: String, val value: String, val comparator: String)