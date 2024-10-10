package fragment.upcoming

import model.UpcomingModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class UpcomingRepository  @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun upcomingRepositoryData(): Response<UpcomingModel> {
        return apiInterface.upcomingApi()
    }
}