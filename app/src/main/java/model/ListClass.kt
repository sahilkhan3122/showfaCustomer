package model

import com.google.gson.annotations.SerializedName

data class ListClass(
    var description: String = "",
    @SerializedName("structured_formatting")
    var structuredFormatting: StructuredFormatting? = null,
    @SerializedName("place_id")
    var placeId:String=""


)