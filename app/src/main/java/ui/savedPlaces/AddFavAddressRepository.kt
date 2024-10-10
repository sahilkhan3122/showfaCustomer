package ui.savedPlaces

import model.AddAddressFavModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class AddFavAddressRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun addFavAddressRepository(hashMap: HashMap<String,String>): Response<AddAddressFavModel> {
        return apiInterface.addFavouriteAddressApi(hashMap)

    }
}