package model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("status") var status: Boolean? = false,
    @SerializedName("message") var message: String = "",
    @SerializedName("otp") var otp: Int? = null,
    @SerializedName("data") var data: Data,
    @SerializedName("booking_info") var bookingInfo: BookingInfo,
    @SerializedName("vehicle_type_list") var vehicleTypeList: ArrayList<VehicleType>,
    @SerializedName("invite_message") var inviteMessage: String = "",
    @SerializedName("cancel_reason") var cancelReason: ArrayList<CancelReason>,
    @SerializedName("sos_number") var sosNumber: String = ""
) {
    data class Data(
        @SerializedName("id") var id: String = "",
        @SerializedName("user_type") var userType: String = "",
        @SerializedName("company_id") var companyId: String = "",
        @SerializedName("corporate_company_id") var corporateCompanyId: String = "",
        @SerializedName("company_name") var companyName: String = "",
        @SerializedName("first_name") var firstName: String = "",
        @SerializedName("last_name") var last_name: String = "",
        @SerializedName("email") var email: String = "",
        @SerializedName("mobile_no") var mobileNo: String = "",
        @SerializedName("dob") var dob: String = "",
        @SerializedName("gender") var gender: String = "",
        @SerializedName("identify_number") var identifyNumber: String = "",
        @SerializedName("wallet_balance") var walletBalance: String = "",
        @SerializedName("saving_wallet_balance") var savingWalletBalance: String = "",
        @SerializedName("saving_wallet_percentage") var savingWalletPercentage: Any = Any(),
        @SerializedName("is_saving_wallet") var isSavingWallet: String = "",
        @SerializedName("miles_balance") var milesBalance: String = "",
        @SerializedName("miles_exp_date") var milesExpDate: String = "",
        @SerializedName("co_miles_balance") var coMilesBalance: String = "",
        @SerializedName("co_miles_exp_date") var coMilesExpDate: String = "",
        @SerializedName("device_type") var deviceType: String = "",
        @SerializedName("device_token") var deviceToken: String = "",
        @SerializedName("old_device_token") var oldDeviceToken: String = "",
        @SerializedName("old_device_type") var oldDeviceType: String = "",
        @SerializedName("lat") var lat: String = "",
        @SerializedName("lng") var lng: String = "",
        @SerializedName("qr_code") var qrCode: String = "",
        @SerializedName("profile_image") var profileImage: String = "",
        @SerializedName("social_id") var socialId: String = "",
        @SerializedName("social_type") var socialType: String = "",
        @SerializedName("remember_token") var rememberToken: String = "",
        @SerializedName("address") var address: String = "",
        @SerializedName("trip_limit_month") var tripLimitMonth: String = "",
        @SerializedName("trash") var trash: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("referral_code") var referralCode: String = "",
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("rating") var rating: String = "",
        @SerializedName("transaction_password") var transactionPassword: String = "",
        @SerializedName("jambopay_profile_id") var jambopayProfileId: String = "",
        @SerializedName("is_jambopay_wallet_setup") var isJambopayWalletSetup: String = "",
        @SerializedName("setup_wallet_response") var setupWalletResponse: String = "",
        @SerializedName("initiate_auto_debit_subscription_reference_no") var initiateAutoDebitSubscriptionReferenceNo: String = "",
        @SerializedName("verify_auto_debit_subscription_response") var verifyAutoDebitSubscriptionResponse: Any = Any(),
        @SerializedName("last_wallet_update_time") var lastWalletUpdateTime: String = "",
        @SerializedName("last_wallet_response") var lastWalletResponse: Any = Any(),
        @SerializedName("x-api-key") var xApiKey: String = ""
    ) {

        data class LastWalletResponse(
            @SerializedName("account") var account: String = "",
            @SerializedName("balance") var balance: String = "",
            @SerializedName("phoneNumber") var phoneNumber: String = "",
            @SerializedName("timestamp") var timestamp: String = "",

            )
    }

    data class BookingInfo(
        @SerializedName("total_trips") var totalTrips: Int = 0
    )

    data class VehicleType(
        @SerializedName("id") var id: String = "",
        @SerializedName("type") var type: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("base_km") var baseKm: String = "",
        @SerializedName("minimum_fare") var minimumFare: String = "",
        @SerializedName("base_charge") var baseCharge: String = "",
        @SerializedName("per_km_charge") var perKmCharge: String = "",
        @SerializedName("per_minute_charge") var perMinuteCharge: String = "",
        @SerializedName("waiting_time_per_min_charge") var waitingTimePerMinCharge: String = "",
        @SerializedName("booking_fee") var bookingFee: String = "",
        @SerializedName("driver_cancellation_fee") var driverCancellationFee: String = "",
        @SerializedName("customer_cancellation_fee") var customerCancellationFee: String = "",
        @SerializedName("free_cancallation_time") var freeCancallationTime: String = "",
        @SerializedName("extra_charge") var extraCharge: String = "",
        @SerializedName("capacity") var capacity: String = "",
        @SerializedName("commission") var commission: String = "",
        @SerializedName("image") var image: String = "",
        @SerializedName("unselect_image") var unselectImage: String = "",
        @SerializedName("sort") var sort: String = "",
        @SerializedName("description") var description: String = "",
        @SerializedName("trash") var trash: String = "",
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "",
        @SerializedName("base") var base: String = "",
        @SerializedName("promotion_amount") var promotionAmount: String = "",
        @SerializedName("charge") var charge: String = "",
        @SerializedName("bulk_mile_rate") var bulkMileRate: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("model_sequence") var modelSequence: String = "",
        @SerializedName("book_later_model_sequence") var bookLaterModelSequence: String = "",
        @SerializedName("vehicle_type") var vehicleType: String = ""
    )
}

data class CancelReason(
    @SerializedName("id") var id: String = "",
    @SerializedName("reason") var reason: String = "",
    @SerializedName("for_whom") var forWhom: String = ""
)
