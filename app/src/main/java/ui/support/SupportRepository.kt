package ui.support

import model.SupportModel
import network.ApiInterface
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class SupportRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun supportRepository( hashMap: HashMap<String,String>): Response<SupportModel> {
        return apiInterface.supportApi(hashMap)
    }
}