package ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import com.google.android.gms.maps.model.LatLng
import model.AddAddressFavModel
import model.DeletAccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laungage.MyDataStore
import retrofit2.Retrofit
import ui.homeActivity.DeleteMyAccountRepository
import ui.savedPlaces.AddFavAddressRepository
import ui.login.LoginViewModel
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class HomeViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {


    var home = context.getString(R.string.home)


    //add fav address
    private val addFavAddressMutableLiveData: MutableLiveData<ShowStatus<AddAddressFavModel>> =
        MutableLiveData()

    var addFavLiveData: LiveData<ShowStatus<AddAddressFavModel>> = addFavAddressMutableLiveData

    @Inject
    lateinit var retrofit: Retrofit



    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var addFavRepository: AddFavAddressRepository

    companion object {
        var TAG = LoginViewModel::class.java.simpleName
    }

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    //deleteMyAccount
    var deleteAccountMutableData: MutableLiveData<ShowStatus<DeletAccountModel>> = MutableLiveData()
    var deleteAccountLiveData: MutableLiveData<ShowStatus<DeletAccountModel>> =
        deleteAccountMutableData

    @Inject
    lateinit var deleteAccountRepository: DeleteMyAccountRepository

    fun addFavApiCall(picklocation: String, pickLat: LatLng?) {

        val hashMap = HashMap<String, String>()
        hashMap["customer_id"] = 100.toString()
        hashMap["favourite_type"] = "other"
        hashMap["pickup_location"] = picklocation
            hashMap["pickup_lat"] = pickLat?.latitude.toString()
        hashMap["pickup_lng"] = pickLat?.longitude.toString()
        hashMap["dropoff_location"] = "dropOffLocation"
        hashMap["dropoff_lat"] = "dropOffLat"
        hashMap["dropoff_lng"] = "dropOffLng"
        hashMap["update_status"] = true.toString()


        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {

                    val repository = addFavRepository.addFavAddressRepository(hashMap)
                    addFavAddressMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                    Log.d(TAG, "SUCCESS=>${repository}")
                } catch (e: Exception) {

                    addFavAddressMutableLiveData.postValue(
                        ShowStatus.error(
                            null,
                            context.getString(R.string.wrong)
                        )
                    )
                    Log.d(TAG, "ERROR APi=>${e}")

                }

            }

        }
    }

    fun deleteAccountApiCall(id: String) {

        val hashMap = HashMap<String, String>()
        hashMap["user_id"] = id
        viewModelScope.launch {
            try {
                var deleteRepository = deleteAccountRepository.deleteMyAccountRepository(hashMap)
                deleteAccountMutableData.postValue(requestCodeCheck.getResponse(deleteRepository))
                Log.d(TAG, "SUCCESS=>${deleteRepository}")


            } catch (e: java.lang.Exception) {

                deleteAccountMutableData.postValue(
                    ShowStatus.error(
                        null,
                        context.getString(R.string.wrong)
                    )
                )
                Log.d(TAG, "ERROR APi=>${e}")
            }
        }

    }
}