package ui.tripHistory

import model.CancelTripModel
import network.ApiInterface
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class CancelTripRepository@Inject constructor(var apiInterface: ApiInterface) {
    suspend fun cancelTripRepository(hashMap: HashMap<String, Int>): Response<CancelTripModel> {
        return apiInterface.cancelTripApi(hashMap)
    }
}