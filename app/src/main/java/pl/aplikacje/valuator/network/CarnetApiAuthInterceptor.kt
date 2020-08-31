package pl.aplikacje.valuator.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Vipul Asri on 31/08/20.
 */

class CarnetApiAuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("api-key", "f786b979-20c1-49bf-bb4b-0f63f3f859db")
            .build()
        return chain.proceed(newRequest)
    }
}