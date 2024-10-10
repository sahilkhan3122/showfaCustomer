package ui.login

import model.LoginModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun getLoginRepository(hashMap: HashMap<String,String>): Response<LoginModel> {
        return apiInterface.loginApi(hashMap)

    }
}