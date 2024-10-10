package model

import com.google.gson.annotations.SerializedName

data class AddAddressFavModel(

    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val `data`: ArrayList<Data>
) {
    data class Data(
        @SerializedName("id") val id: String = "",
        @SerializedName("customer_id") val customerId: String = "",
        @SerializedName("favourite_type") val favouriteType: String = "",
        @SerializedName("pickup_location") val pickupLocation: String = "",
        @SerializedName("pickup_lat") val pickupLat: String = "",
        @SerializedName("pickup_lng") val pickupLng: String = "",
        @SerializedName("dropoff_location") val dropoffLocation: String = "",
        @SerializedName("dropoff_lat") val dropoffLat: String = "",
        @SerializedName("dropoff_lng") val dropoffLng: String = ""

    )
}