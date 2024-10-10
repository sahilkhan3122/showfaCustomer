package ui.tripHistory

import model.TripDetailModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class TripRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun tripRepositoryData(): Response<TripDetailModel> {
        return apiInterface.tripDetailApi()
    }
}