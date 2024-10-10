package model

import com.google.gson.annotations.SerializedName

data class NotificationListModel(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("data") val `data`: ArrayList<Data>
) {
    data class Data(
        @SerializedName("id") val id: String = "",
        @SerializedName("user_type") val user_type: String = "",
        @SerializedName("user_id") val user_id: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("image") val image:String="",
        @SerializedName("read_status_user") val readStatusUser: String = "",
        @SerializedName("created_date") val createdDate: String = ""
    )
}