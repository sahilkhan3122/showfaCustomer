package model

import com.google.gson.annotations.SerializedName

data class ProfileUpdateModel(
    var status: Boolean = false,
    var message: String = "",
    var `data`: Data
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
        var profile_image: String = "",
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
        var jambopay_profile_id: String = "",
        var is_jambopay_wallet_setup: String = "",
        var setup_wallet_response: String = "",
        var initiate_auto_debit_subscription_reference_no: String = "",
        var verify_auto_debit_subscription_response: Any = Any(),
        var last_wallet_update_time: Any = Any(),
        var last_wallet_response: Any = Any(),
        @SerializedName("x-api-key")
        var xApiKey: String = ""
    )
}