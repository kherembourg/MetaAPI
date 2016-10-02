package net.apigator.metaapi

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {

    @POST("couchi2/couchi/json/searchR/")
    fun postData(@Body data: SMObject) : Call<ResponseBody>
}