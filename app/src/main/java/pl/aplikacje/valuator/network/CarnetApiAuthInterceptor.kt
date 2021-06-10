package pl.aplikacje.valuator.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Vipul Asri on 31/08/20.
 */

class CarnetApiAuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("", "")
            .build()
        return chain.proceed(newRequest)
    }
}
