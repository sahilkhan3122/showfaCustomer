package fragment.upcoming

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import model.UpcomingModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class UpcomingViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    var upcomingMutableData: MutableLiveData<ShowStatus<UpcomingModel>> = MutableLiveData()
    var upcomingLiveData: LiveData<ShowStatus<UpcomingModel>> = upcomingMutableData

    @Inject
    lateinit var upcomingRepository: UpcomingRepository

    var TAG = UpcomingViewModel::class.java.simpleName

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck


    fun upcomingApiCall() {
         viewModelScope.launch {
            try {
                val upcomingRepository = upcomingRepository.upcomingRepositoryData()
                upcomingMutableData.postValue(requestCodeCheck.getResponse(upcomingRepository))
                Log.d(TAG, "SUCCESS===>")

            } catch (e: Exception) {
                upcomingMutableData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERROR===>$e")
            }
        }
    }
}