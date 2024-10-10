package model

data class SplashModel(
    var status: Boolean,
    var booking_info: BookingInfo = BookingInfo(),
    var vehicle_type_list: List<VehicleType> = listOf(),
    var currency: String = "",
    var is_expired: Int = 0,
    var ios_update: String = "",
    var cancel_reason: List<CancelReason> = listOf(),
    var invite_message: String = "",
    var sos_number: String = "",
    var privacy_policy_url: String = "",
    var terms_condition_url: String = "",
    var otp_auto_fill: Boolean = false,
    var otp_auto_fill_ios: Boolean = false,
    var facebook_enable: Boolean = false,
    var socket_url: String = "",
    var image_base_url: String = ""
) {
    data class BookingInfo(
        val total_trips: Int = 0
    )

    data class VehicleType(
        val id: String = "",
        val type: String = "",
        val name: String = "",
        val base_km: String = "",
        val minimum_fare: String = "",
        val base_charge: String = "",
        val per_km_charge: String = "",
        val per_minute_charge: String = "",
        val waiting_time_per_min_charge: String = "",
        val booking_fee: String = "",
        val driver_cancellation_fee: String = "",
        val customer_cancellation_fee: String = "",
        val free_cancallation_time: String = "",
        val extra_charge: String = "",
        val capacity: String = "",
        val commission: String = "",
        val image: String = "",
        val unselect_image: String = "",
        val sort: String = "",
        val description: String = "",
        val trash: String = "",
        val created_at: String = "",
        val updated_at: String = "",
        val base: String = "",
        val promotion_amount: String = "",
        val charge: String = "",
        val bulk_mile_rate: String = "",
        val status: String = "",
        val model_sequence: String = "",
        val book_later_model_sequence: String = "",
        val vehicle_type: String = ""
    )

    data class CancelReason(
        val id: String = "",
        val reason: String = "",
        val for_whom: String = ""
    )
}