package pl.aplikacje.valuator.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.aplikacje.valuator.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkUtils {

    private val BASE_URL = "https://api.carnet.ai/v2/mmg/"

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addNetworkInterceptor(CarnetApiAuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

    val uploadService = buildService(UploadService::class.java)
}