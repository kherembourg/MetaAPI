package net.apigator.metaapi

data class SMObject(val name: String,
                    val params: SMParams? = null,
                    val children: List<SMObject>? = null,
                    val filters: List<SMFilters>? = null)

data class SMParams(val lat: Float, val lng: Float)

data class SMFilters(val name: String, val value: String, val comparator: String)