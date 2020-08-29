package pl.aplikacje.valuator.Model

import android.net.Uri
import com.google.gson.annotations.SerializedName



class Communication {

    @SerializedName("make_name")
    private var make_name: String? = null

    @SerializedName("model_name")
    private var model_name: String? = null

    @SerializedName("generation_name")
    private var generation_name: String? = null

    @SerializedName("years")
    private var years: String? = null

    fun Communication( make_name: String?, model_name: String?, generation_name: String?, years: String?)
    {
        this.make_name = make_name
        this.model_name = model_name
        this.generation_name = generation_name
        this.years = years
    }


    fun getMake(): String? {
        return make_name
    }

//    fun setMake(make_name: String?) {
//        this.make_name = make_name
//    }

    fun getModel(): String? {
        return model_name
    }

//    fun setModel(model_name: String?) {
//        this.model_name = model_name
//    }

    fun getGeneration(): String? {
        return generation_name
    }

//    fun setGeneration(generation_name: String?) {
//        this.generation_name = generation_name
//    }

    fun getYears(): String? {
        return years
    }

//    fun setYears(years: String?) {
//        this.years = years
//    }

}