package ui.savedPlaces

import model.FavAddressListModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class FavAddressListRepository @Inject constructor(var apiInterface: ApiInterface) {
     suspend fun favAddressRepository(uri:String): Response<FavAddressListModel> {
        return apiInterface.getFavAddressList(uri)
    }
}