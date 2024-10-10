package utils

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object API_CONSTANTS {


    //    const val URL: String = "http://65.2.100.100/"
    const val URL: String = "https://dev.showfaride.com/"
    const val BASE_URL = "${URL}api/v3/customer_api/"
    const val initApi = "${BASE_URL}init/ios_customer/1.0.0/100"
    const val loginApi = "${BASE_URL}login"
    const val registerApi = "${BASE_URL}register"
    const val otpRegisterApi = "${BASE_URL}register_otp"
    const val resendOtp = "${BASE_URL}resend_otp"
    const val recentPlaceListApi = "${BASE_URL}recent_place_list"
    const val saveRecentPlaceApi = "${BASE_URL}save_recent_place"
    const val profileApi = "${BASE_URL}profile_update"
    const val pastApi = "${BASE_URL}past_booking_history/100"
    const val upcomingApi = "${BASE_URL}upcoming_booking_history/100"
    const val tripDetailsApi = "${BASE_URL}trip_details/641"
    const val previousApi = "${BASE_URL}past_due_history/30"
    const val walletApi = "${BASE_URL}wallet_history"
    const val promoCodesApi = "${BASE_URL}promocode_list"
    const val supportTicketListApi = "${BASE_URL}ticket_list/100"
    const val supportApi = "${BASE_URL}generate_ticket"
    const val deleteMyAccountApi = "${BASE_URL}delete_account"
    const val addFavouriteAddress = "${BASE_URL}add_favourite_address"
    const val favouriteAddressList = "${BASE_URL}favourite_address_list/"
    const val notificationListApi = "${BASE_URL}notification_list/"
    const val notificationClearApi = "${BASE_URL}notification_clear/"
    const val cancelTripApi = "${BASE_URL}cancel_trip"
    const val addMoneyWalletApi = "${BASE_URL}add_money"
    const val sendMoneyWalletApi = "${BASE_URL}transfer_money_with_mobile_no"
    const val transferMoneyWithMobileNoCheckApi = "${BASE_URL}transfer_money_with_mobile_no_check"
    const val downloadInvoice =
        "https://dev.showfaride.com/api/v3/customer_api/download_invoice/2072"


    var webview = "webviewUrl"
    var TERMSANDCONDITIONSET = ""
    var PRIVACYANDPOLICY = ""
    var X_API_KEY = ""
    var RegisterX_api_key = ""
    var LOGINOTP = ""
    var REGISTEROTP = ""
    var MOBILENUMBER = ""
    var FIRSTNAME = ""
    var LASTNAME = ""
    var EMAIL = ""
    var ADDRESS = ""
    var DOB = ""
    var NATIONALID = ""
    var REFERRALCODE = ""
    var DATEBIRTH = ""
    var PROFILEIMAGE = ""
    var GENDER = ""
    var AMOUNT = ""
    var imagUri: Uri? = null



    //saved places autoIntent
    var mutableLiveData: MutableLiveData<String> = MutableLiveData()
    var liveData: LiveData<String> =mutableLiveData


}

