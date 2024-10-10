package model

import com.google.gson.annotations.SerializedName

data class FavAddressListModel(
     @SerializedName("status") var status: Boolean = false,
     @SerializedName("data") var `data`: ArrayList<Data>
) {
    data class Data(
        @SerializedName("id") var id: String = "",
        @SerializedName("customer_id") var customerId: String = "",
        @SerializedName("favourite_type") var favouriteType: String = "",
        @SerializedName("pickup_location") var pickupLocation: String = "",
        @SerializedName("pickup_lat") var pickupLat: String = "",
        @SerializedName("pickup_lng") var pickupLng: String = "",
        @SerializedName("dropoff_location") var dropoffLocation: String = "",
        @SerializedName("dropoff_lat") var dropoffLat: String = "",
        @SerializedName("dropoff_lng") var dropoffLng: String = ""
    )
}