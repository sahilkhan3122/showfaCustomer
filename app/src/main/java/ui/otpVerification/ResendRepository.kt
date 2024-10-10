package ui.otpVerification

import model.ResendOTPModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class ResendRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun setResendOtpRepository(hashMap: HashMap<String,String>):Response<ResendOTPModel>{
        return apiInterface.resendOtp(hashMap)
    }
}