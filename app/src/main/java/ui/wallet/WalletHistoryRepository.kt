package ui.wallet

import model.WalletHistoryModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class WalletHistoryRepository@Inject constructor(var apiInterface: ApiInterface) {
    suspend fun walletRepository(hashMap: HashMap<String,String>): Response<WalletHistoryModel> {
        return apiInterface.walletApi(hashMap)
    }
}