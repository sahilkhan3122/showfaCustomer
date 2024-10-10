package model

import com.google.gson.annotations.SerializedName

data class RegisterModel(
    val status: Boolean = false,
    val message: String = "",
    val `data`: Data = Data(),
    val booking_info: BookingInfo = BookingInfo(),
    val vehicle_type_list: List<VehicleType> = listOf(),
    val invite_message: String = "",
    val sos_number: String = ""
) {
    data class Data(
        var id: String = "",
        var user_type: String = "",
        var company_id: String = "",
        var corporate_company_id: String = "",
        var company_name: String = "",
        var first_name: String = "",
        var last_name: String = "",
        var email: String = "",
        var mobile_no: String = "",
        var dob: String = "",
        var gender: String = "",
        var identify_number: String = "",
        var wallet_balance: String = "",
        var saving_wallet_balance: String = "",
        var saving_wallet_percentage: Any = Any(),
        var is_saving_wallet: String = "",
        var miles_balance: String = "",
        var miles_exp_date: String = "",
        var co_miles_balance: String = "",
        var co_miles_exp_date: String = "",
        var device_type: String = "",
        var device_token: String = "",
        var old_device_token: String = "",
        var old_device_type: String = "",
        var lat: String = "",
        var lng: String = "",
        var qr_code: String = "",
        var profile_image : String = "",
        var social_id: String = "",
        var social_type: String = "",
        var remember_token: String = "",
        var address: String = "",
        var trip_limit_month: String = "",
        var trash: String = "",
        var status: String = "",
        var referral_code: String = "",
        var created_at: String = "",
        var rating: String = "",
        var transaction_password: String = "",
        var jambopay_profile_id: Any = Any(),
        var is_jambopay_wallet_setup: String = "",
        var setup_wallet_response: Any = Any(),
        var initiate_auto_debit_subscription_reference_no: Any = Any(),
        var verify_auto_debit_subscription_response: Any = Any(),
        var last_wallet_update_time: Any = Any(),
        var last_wallet_response: Any = Any(),
        @SerializedName("x-api-key")
        val xApiKey: String = ""
    )

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
}