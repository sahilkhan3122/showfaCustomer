package ui.register

import model.ProfileUpdateModel
import network.ApiInterface
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ProfileUpdateRepository@Inject constructor(var apiInterface: ApiInterface) {
    suspend fun getProfile(hashMap: HashMap<String,RequestBody>): Response<ProfileUpdateModel> {
        return apiInterface.profileApi(hashMap)
    }
}