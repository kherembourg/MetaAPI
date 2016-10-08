package net.apigator.metaapi

import android.app.Application
import android.webkit.WebView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {

    lateinit var retrofit : Retrofit

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.tag("OkHttp").d(message) })
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttp = OkHttpClient.Builder()
        okHttp.addInterceptor(logging)
        okHttp.connectTimeout(30, TimeUnit.SECONDS)
        okHttp.readTimeout(30, TimeUnit.SECONDS)

        retrofit = Retrofit.Builder()
                .baseUrl("https://api-java.apigator.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttp.build())
                .build()

        Timber.plant(Timber.DebugTree())

        WebView.setWebContentsDebuggingEnabled(true)
    }

}