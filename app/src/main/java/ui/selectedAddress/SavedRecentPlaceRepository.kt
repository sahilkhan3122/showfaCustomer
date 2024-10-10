package ui.selectedAddress

import model.SaveRecentPlayListModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class SavedRecentPlaceRepository  @Inject constructor(var apiInterface: ApiInterface){
    suspend fun savedPlacesList(hashMap: HashMap<String,String>): Response<SaveRecentPlayListModel> {
        return apiInterface.saveRecentApi(hashMap)
    }
}