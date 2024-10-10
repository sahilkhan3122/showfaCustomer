package network

import model.LoginModel
import model.SplashModel
import model.DownloadInvoiceModel
import model.OtpVerifyModel
import model.PastModel
import model.PreviousDueModel
import model.ProfileUpdateModel
import model.PromocodsModel
import model.RegisterModel
import model.ResendOTPModel
import model.SaveRecentPlayListModel
import model.SupportModel
import model.TripDetailModel
import model.UpcomingModel
import model.WalletHistoryModel
import model.AddAddressFavModel
import model.CancelTripModel
import model.CheckMobileNumberCustomerDriverModel
import model.DeletAccountModel
import model.FavAddressListModel
import model.NotificationClearModel
import model.NotificationListModel
import model.SendWalletMoneyModel
import model.WalletAddMoneyModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Url
import utils.API_CONSTANTS.addFavouriteAddress
import utils.API_CONSTANTS.addMoneyWalletApi
import utils.API_CONSTANTS.cancelTripApi
import utils.API_CONSTANTS.deleteMyAccountApi
import utils.API_CONSTANTS.downloadInvoice
import utils.API_CONSTANTS.initApi
import utils.API_CONSTANTS.loginApi
import utils.API_CONSTANTS.otpRegisterApi
import utils.API_CONSTANTS.pastApi
import utils.API_CONSTANTS.previousApi
import utils.API_CONSTANTS.profileApi
import utils.API_CONSTANTS.promoCodesApi
import utils.API_CONSTANTS.recentPlaceListApi
import utils.API_CONSTANTS.registerApi
import utils.API_CONSTANTS.resendOtp
import utils.API_CONSTANTS.saveRecentPlaceApi
import utils.API_CONSTANTS.sendMoneyWalletApi
import utils.API_CONSTANTS.supportApi
import utils.API_CONSTANTS.transferMoneyWithMobileNoCheckApi
import utils.API_CONSTANTS.tripDetailsApi
import utils.API_CONSTANTS.upcomingApi
import utils.API_CONSTANTS.walletApi

interface ApiInterface {

    @GET(initApi)
    suspend fun initApiCall(): Response<SplashModel>

    @FormUrlEncoded
    @POST(loginApi)
    suspend fun loginApi(@FieldMap hashMap: HashMap<String, String>): Response<LoginModel>

    @Multipart
    @POST(registerApi)
    suspend fun registerApi(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part profile_image: MultipartBody.Part? = null
    ): Response<RegisterModel>

    @FormUrlEncoded
    @POST(otpRegisterApi)
    suspend fun otpRegisterApi(@FieldMap hashMap: HashMap<String, String>): Response<OtpVerifyModel>

    @FormUrlEncoded
    @POST(resendOtp)
    suspend fun resendOtp(@FieldMap hashMap: HashMap<String, String>): Response<ResendOTPModel>

    @FormUrlEncoded
    @POST(recentPlaceListApi)
    suspend fun recentPlaceListApi(@FieldMap hashMap: HashMap<String, Int>): Response<SaveRecentPlayListModel>

    @FormUrlEncoded
    @POST(saveRecentPlaceApi)
    suspend fun saveRecentApi(@FieldMap hashMap: HashMap<String, String>): Response<SaveRecentPlayListModel>


    @Multipart
    @POST(profileApi)
    suspend fun profileApi(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part profile_image: MultipartBody.Part? = null
    ): Response<ProfileUpdateModel>

    @GET(pastApi)
    suspend fun pastApi(): Response<PastModel>

    @GET(upcomingApi)
    suspend fun upcomingApi(): Response<UpcomingModel>

    @GET(tripDetailsApi)
    suspend fun tripDetailApi(): Response<TripDetailModel>

    @GET(downloadInvoice)
    suspend fun downloadInvoiceApi(): Response<DownloadInvoiceModel>

    @GET(previousApi)
    suspend fun previousApi(): Response<PreviousDueModel>

    @GET(promoCodesApi)
    suspend fun promoCodesApi(): Response<PromocodsModel>

    @FormUrlEncoded
    @POST(supportApi)
    suspend fun supportApi(@FieldMap hashMap: HashMap<String, String>): Response<SupportModel>

    @FormUrlEncoded
    @POST(walletApi)
    suspend fun walletApi(@FieldMap hashMap: HashMap<String, String>): Response<WalletHistoryModel>


    @FormUrlEncoded
    @POST(addFavouriteAddress)
    suspend fun addFavouriteAddressApi(@FieldMap hashMap: HashMap<String, String>): Response<AddAddressFavModel>

    @FormUrlEncoded
    @POST(deleteMyAccountApi)
    suspend fun deleteAccountAddressApi(@FieldMap hashMap: HashMap<String, String>): Response<DeletAccountModel>

    @GET
    suspend fun notificationListApi(@Url url: String): Response<NotificationListModel>

    @GET
    suspend fun getFavAddressList(@Url url: String): Response<FavAddressListModel>

    @GET
    suspend fun notificationLClearApi(@Url url: String): Response<NotificationClearModel>


    @FormUrlEncoded
    @POST(cancelTripApi)
    suspend fun cancelTripApi(@FieldMap hashMap: HashMap<String, Int>): Response<CancelTripModel>

    @FormUrlEncoded
    @POST(addMoneyWalletApi)
    suspend fun walletAddMoneyApi(@FieldMap hashMap: HashMap<String, String>): Response<WalletAddMoneyModel>

    @FormUrlEncoded
    @POST(sendMoneyWalletApi)
    suspend fun sendMoneyWalletApi(@FieldMap hashMap: HashMap<String, String>): Response<SendWalletMoneyModel>

    @FormUrlEncoded
    @POST(transferMoneyWithMobileNoCheckApi)
    suspend fun transferMoneyWithMobileNoCheckApi(@FieldMap hashMap: HashMap<String, String>): Response<CheckMobileNumberCustomerDriverModel>


}