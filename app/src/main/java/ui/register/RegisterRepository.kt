package ui.register

import model.RegisterModel
import network.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RegisterRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun getRegisterRepository(
        hashMap: HashMap<String,RequestBody>,image:MultipartBody.Part): Response<RegisterModel> {
        return apiInterface.registerApi(hashMap,image)

    }
}