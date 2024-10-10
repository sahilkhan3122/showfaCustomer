package ui.savedPlaces

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import model.AddAddressFavModel
import kotlinx.coroutines.launch
import model.FavAddressListModel
import network.ApiInterface
import retrofit2.Retrofit
import ui.otpVerification.OtpViewModel
import ui.selectedAddress.SavedRecentPlaceRepository
import utils.API_CONSTANTS.favouriteAddressList
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class SavedPlacesViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    companion object {
        var TAG: String = OtpViewModel::class.java.simpleName
    }

    private val addFavAddressMutableData: MutableLiveData<ShowStatus<AddAddressFavModel>> =
        MutableLiveData()

    fun addFavAddressLiveData(): LiveData<ShowStatus<AddAddressFavModel>> {
        return addFavAddressMutableData
    }

    @Inject
    lateinit var favAddressListRepository: FavAddressListRepository

    var favAddressListMutableData: MutableLiveData<ShowStatus<FavAddressListModel>> =
        MutableLiveData()

    var favAddressListAddressLiveData: LiveData<ShowStatus<FavAddressListModel>> =
        favAddressListMutableData


    @Inject
    lateinit var retrofit: Retrofit

    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var addFavAddressRepository: AddFavAddressRepository

    @Inject
    lateinit var savedRecentPlaceRepository: SavedRecentPlaceRepository


    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck


    private fun getString(plzEnterEmail: Int): String {
        return context.resources.getString(plzEnterEmail)
    }

    fun addFavApicall(
        id: String,
        location: String,
        pickLat: String,
        pickLng: String,
        favtype: String
    ) {
        val hashMap = HashMap<String, String>()

        Log.d("TAG", "ID==>$id")
        Log.d("TAG", "location==>$location")
        Log.d("TAG", "pickLat==>$pickLat")
        Log.d("TAG", "pickLng==>$pickLng")

        hashMap["customer_id"] = id
        hashMap["favourite_type"] = favtype
        hashMap["pickup_location"] = location
        hashMap["pickup_lat"] = pickLat
        hashMap["pickup_lng"] = pickLng
        hashMap["update_status"] = true.toString()

        viewModelScope.launch {
            try {
                val repository = addFavAddressRepository.addFavAddressRepository(hashMap)
                addFavAddressMutableData.postValue(requestCodeCheck.getResponse(repository))
                Log.d("Data", "Success------>$repository")

            } catch (e: Exception) {

                addFavAddressMutableData.value =
                    ShowStatus.error(null, getString(R.string.wrong))
                Log.d(
                    "Data", "fail---->${
                        e.toString()
                    }"
                )


            }
        }

    }

    fun getFavListApiCall(id: String) {
        Log.d(TAG, "id----->$id")
        viewModelScope.launch {
            try {
                val repository =
                    favAddressListRepository.favAddressRepository(favouriteAddressList + id)
                favAddressListMutableData.postValue(requestCodeCheck.getResponse(repository))
                Log.d("Data", "Success------>$repository")

            } catch (e: Exception) {

                favAddressListMutableData.value =
                    ShowStatus.error(null, getString(R.string.wrong))
                Log.d(
                    "Data", "fail---->${
                        e.toString()
                    }"
                )


            }
        }

    }
}
