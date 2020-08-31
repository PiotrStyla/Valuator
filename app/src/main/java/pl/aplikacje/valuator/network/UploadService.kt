package pl.aplikacje.valuator.network

import android.util.ArrayMap
import okhttp3.RequestBody
import pl.aplikacje.valuator.model.CarnetDetectResponse
import retrofit2.Call
import retrofit2.http.*


interface UploadService {

    @POST("detect")
    fun upload(
        @QueryMap parameters: Map<String, String>,
        @Body file: RequestBody
    ): Call<CarnetDetectResponse>
}