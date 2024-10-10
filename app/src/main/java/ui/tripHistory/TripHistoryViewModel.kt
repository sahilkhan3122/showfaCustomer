package ui.tripHistory

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import kotlinx.coroutines.Dispatchers
import model.TripDetailModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laungage.MyDataStore
import model.CancelTripModel
import retrofit2.Retrofit
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class TripHistoryViewModel @Inject constructor(var context: Context):BaseViewModel<Any>(){

    companion object {
        var TAG = TripHistoryViewModel::class.java.simpleName
    }
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore
    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var tripRepository: TripRepository

    private val tripMutableLiveDta: MutableLiveData<ShowStatus<TripDetailModel>> = MutableLiveData()

    fun tripLiveData(): LiveData<ShowStatus<TripDetailModel>> {
        return tripMutableLiveDta
    }

    @Inject
    lateinit var cancelTripRepository: CancelTripRepository

    private val cancelTripMutableLiveDta: MutableLiveData<ShowStatus<CancelTripModel>> = MutableLiveData()

    fun cancelTripLiveData(): LiveData<ShowStatus<CancelTripModel>> {
        return cancelTripMutableLiveDta
    }

    fun tripApiCall(){
        viewModelScope.launch {
            try {
                val repository=tripRepository.tripRepositoryData()
                tripMutableLiveDta.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS APi")

            }catch (e:Exception){
                tripMutableLiveDta.value= ShowStatus(Status.ERROR,null)
                Log.d(TAG, "ERROR APi=>${e}")


            }
        }

    }
     fun cancelTripApiCall(id:String){
        val hashmap=HashMap<String,Int>()
        hashmap["booking_id"] = id.toInt()
         viewModelScope.launch {
             withContext(Dispatchers.Main){
                 try {
                     val repository=cancelTripRepository.cancelTripRepository(hashmap)
                     cancelTripMutableLiveDta.postValue(requestCodeCheck.getResponse(repository))
                     Log.d(TAG, "cancelTripApiCall SUCCESS APi")

                 }catch (e:Exception){
                     cancelTripMutableLiveDta.value= ShowStatus(Status.ERROR,null)
                     Log.d(TAG, "cancelTripApiCall ERROR APi=>${e}")


                 }


             }

        }
    }

}