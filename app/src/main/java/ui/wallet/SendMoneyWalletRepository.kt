package ui.wallet

import model.SendWalletMoneyModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class SendMoneyWalletRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun walletRepository(hashMap: HashMap<String,String>): Response<SendWalletMoneyModel> {
        return apiInterface.sendMoneyWalletApi(hashMap)
    }
}