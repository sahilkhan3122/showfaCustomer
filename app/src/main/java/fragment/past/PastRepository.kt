package fragment.past

import model.PastModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class PastRepository @Inject constructor(var apiInterface:ApiInterface){
    suspend fun pastRepositoryData():Response<PastModel>{
       return apiInterface.pastApi()
    }
}