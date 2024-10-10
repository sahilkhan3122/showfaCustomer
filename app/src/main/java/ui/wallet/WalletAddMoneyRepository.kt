package ui.wallet

import model.CancelTripModel
import model.WalletAddMoneyModel
import network.ApiInterface
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class WalletAddMoneyRepository@Inject constructor(var apiInterface: ApiInterface) {
    suspend fun walletAddMoneyRepository(hashMap: HashMap<String, String>): Response<WalletAddMoneyModel> {
        return apiInterface.walletAddMoneyApi(hashMap)
    }
}