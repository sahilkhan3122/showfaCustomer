package ui.selectedAddress

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import model.SaveRecentPlayListModel
import kotlinx.coroutines.launch
import network.ApiInterface
import retrofit2.Retrofit
import ui.otpVerification.OtpViewModel
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class SelectedViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    companion object {
        var TAG = OtpViewModel::class.java.simpleName
        fun getSavedListLiveData(selectedViewModel: SelectedViewModel): LiveData<ShowStatus<SaveRecentPlayListModel>> {
            return selectedViewModel.saveAddressListMutableData
        }
    }

    private val saveAddressListMutableData: MutableLiveData<ShowStatus<SaveRecentPlayListModel>> =
        MutableLiveData()
    private val saveRecentAddressPlaceMutableData: MutableLiveData<ShowStatus<SaveRecentPlayListModel>> =
        MutableLiveData()

    fun getSavedRecentPlaceLiveData(): LiveData<ShowStatus<SaveRecentPlayListModel>> {
        return saveRecentAddressPlaceMutableData
    }

    @Inject
    lateinit var retrofit: Retrofit
    lateinit var apiInterface: ApiInterface

//    @Inject
//    lateinit var saveRecentPlayListRepository: savePlacesRepository

    @Inject
    lateinit var savedRecentPlaceRepository: SavedRecentPlaceRepository


    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck


    private fun getString(plzEnterEmail: Int): String {
        return context.resources.getString(plzEnterEmail)
    }


//    fun savedAddressApiCall() {
//        val hashMap = HashMap<String, Int>()
//        hashMap["customer_id"] = 100
//
//        viewModelScope.launch {
//            try {
//                val repository = saveRecentPlayListRepository.savedPlacesList(hashMap)
//                saveAddressListMutableData.postValue(requestCodeCheck.getResponse(repository))
//                Log.d("Data", "Success------>$repository")
//
//            } catch (e: Exception) {
//
//                saveAddressListMutableData.value =
//                    ShowStatus.error(null, getString(R.string.wrong))
//                Log.d(
//                    "Data", "fail---->${
//                        e.toString()
//                    }"
//                )
//
//
//            }
//        }
//
//    }

    fun savedRecentAddressPlaceApiCall(tittle:String,address:String,latitude:Double,longitude:Double) {
        val hashMap = HashMap<String, String>()
        hashMap["customer_id"] = 100.toString()
        hashMap["title"] = tittle
        hashMap["address"] = address
        hashMap["latitude"] = latitude.toString()
        hashMap["longitude"] = longitude.toString()


        viewModelScope.launch {
            try {
                var repository = savedRecentPlaceRepository.savedPlacesList(hashMap)
                saveRecentAddressPlaceMutableData.postValue(requestCodeCheck.getResponse(repository))
                Log.d("Data", "Success")

            } catch (e: Exception) {

                saveRecentAddressPlaceMutableData.value =
                    ShowStatus.error(null, getString(R.string.wrong))
                Log.d(
                    "Data", "fail${
                        e.toString()
                    }"
                )


            }
        }

    }
}