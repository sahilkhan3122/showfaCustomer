package ui.otpVerification

import model.OtpVerifyModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class OtpVerifyRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun getOtpVerify(hashMap: HashMap<String,String>):Response<OtpVerifyModel> {
        return apiInterface.otpRegisterApi(hashMap)
    }
}