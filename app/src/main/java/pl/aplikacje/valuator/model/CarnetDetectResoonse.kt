package pl.aplikacje.valuator.model
import com.google.gson.annotations.SerializedName


data class CarnetDetectResponse(
    @SerializedName("detections")
    val detections: List<Detection>,
    @SerializedName("error")
    val error: Error,
    @SerializedName("is_success")
    val isSuccess: Boolean,
    @SerializedName("meta")
    val meta: Meta
)

data class Detection(
    @SerializedName("angle")
    val angle: List<Angle>,
    @SerializedName("box")
    val box: Box,
    @SerializedName("class")
    val classX: Class,
    @SerializedName("color")
    val color: List<Color>,
    @SerializedName("mm")
    val mm: List<Mm>,
    @SerializedName("mmg")
    val mmg: List<Mmg>,
    @SerializedName("status")
    val status: Status,
    @SerializedName("subclass")
    val subclass: List<Subclas>
)

data class Error(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)

data class Meta(
    @SerializedName("classifier")
    val classifier: Int,
    @SerializedName("md5")
    val md5: String,
    @SerializedName("parameters")
    val parameters: Parameters,
    @SerializedName("time")
    val time: Double
)

data class Angle(
    @SerializedName("name")
    val name: String,
    @SerializedName("probability")
    val probability: Double
)

data class Box(
    @SerializedName("br_x")
    val brX: Double,
    @SerializedName("br_y")
    val brY: Double,
    @SerializedName("tl_x")
    val tlX: Double,
    @SerializedName("tl_y")
    val tlY: Double
)

data class Class(
    @SerializedName("name")
    val name: String,
    @SerializedName("probability")
    val probability: Double
)

data class Color(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("probability")
    val probability: Double
)

data class Mm(
    @SerializedName("make_id")
    val makeId: Int,
    @SerializedName("make_name")
    val makeName: String,
    @SerializedName("model_id")
    val modelId: Int,
    @SerializedName("model_name")
    val modelName: String,
    @SerializedName("probability")
    val probability: Double
)

data class Mmg(
    @SerializedName("generation_id")
    val generationId: Int,
    @SerializedName("generation_name")
    val generationName: String,
    @SerializedName("make_id")
    val makeId: Int,
    @SerializedName("make_name")
    val makeName: String,
    @SerializedName("model_id")
    val modelId: Double,
    @SerializedName("model_name")
    val modelName: String,
    @SerializedName("probability")
    val probability: Double,
    @SerializedName("years")
    val years: String
)

data class Status(
    @SerializedName("code")
    val code: Double,
    @SerializedName("message")
    val message: String,
    @SerializedName("selected")
    val selected: Boolean
)

data class Subclas(
    @SerializedName("name")
    val name: String,
    @SerializedName("probability")
    val probability: Double
)

data class Parameters(
    @SerializedName("box_max_ratio")
    val boxMaxRatio: Double,
    @SerializedName("box_min_height")
    val boxMinHeight: Double,
    @SerializedName("box_min_ratio")
    val boxMinRatio: Double,
    @SerializedName("box_min_width")
    val boxMinWidth: Double,
    @SerializedName("box_offset")
    val boxOffset: Double,
    @SerializedName("box_select")
    val boxSelect: String,
    @SerializedName("features")
    val features: List<String>,
    @SerializedName("region")
    val region: List<String>
)