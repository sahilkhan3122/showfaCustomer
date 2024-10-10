package ui.previousDue

import model.PreviousDueModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class PreviousRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun previousDueRepository():Response<PreviousDueModel>{
        return apiInterface.previousApi()
    }
}