package net.apigator.metaapi

import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface APIService {

    @POST("couchi2/couchi/json/searchR/")
    fun postData(@Body data: SMObject): Observable<Result>
}