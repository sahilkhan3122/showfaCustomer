package ui.wallet

import model.CheckMobileNumberCustomerDriverModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class CheckMobileNumberRepository  @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun checkMobileRepository(hashMap: HashMap<String,String>): Response<CheckMobileNumberCustomerDriverModel> {
        return apiInterface.transferMoneyWithMobileNoCheckApi(hashMap)
    }
}