package ui.homeActivity

import model.DeletAccountModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class DeleteMyAccountRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun deleteMyAccountRepository(hashMap: HashMap<String, String>): Response<DeletAccountModel> {
        return apiInterface.deleteAccountAddressApi(hashMap)
    }
}