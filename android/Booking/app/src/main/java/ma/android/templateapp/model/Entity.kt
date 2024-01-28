package ma.android.templateapp.model

import com.google.gson.annotations.SerializedName

data class Entity (
    @field:SerializedName("id")
    var id: Int = 0,
    @field:SerializedName("nume")
    var name: String = "",
    @field:SerializedName("doctor")
    var doctor: String = "",
    @field:SerializedName("data")
    var date: Int = 0,
    @field:SerializedName("ora")
    var hour: Int = 0,
    @field:SerializedName("detalii")
    var details : String = "",
    @field:SerializedName("status")
    var status : Boolean = false
)