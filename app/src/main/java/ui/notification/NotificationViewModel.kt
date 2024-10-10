package ui.notification

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laungage.MyDataStore
import model.NotificationClearModel
import model.NotificationListModel
import retrofit2.Retrofit
import utils.API_CONSTANTS.notificationClearApi
import utils.API_CONSTANTS.notificationListApi
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class NotificationViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {
    companion object {
        var TAG = NotificationViewModel::class.java.simpleName
    }

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    private val notificationMutableData: MutableLiveData<ShowStatus<NotificationListModel>> =
        MutableLiveData()

    fun notificationLiveData(): LiveData<ShowStatus<NotificationListModel>> =
        notificationMutableData

    private val notificationClearMutableData: MutableLiveData<ShowStatus<NotificationClearModel>> =
        MutableLiveData()

    fun notificationClearLiveData(): LiveData<ShowStatus<NotificationClearModel>> =
        notificationClearMutableData


    fun notificationApiCall(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val repository =
                        notificationRepository.notificationRepository(notificationListApi + id)
                    notificationMutableData.postValue(requestCodeCheck.getResponse(repository))
                    Log.d(TAG, "SUCCESS=>${repository}")
                } catch (e: Exception) {
                    notificationMutableData.postValue(
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

    fun notificationClearApiCall(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val repository =
                        notificationRepository.notificationClearRepository(notificationClearApi + id)
                    notificationClearMutableData.postValue(requestCodeCheck.getResponse(repository))
                    Log.d(TAG, "SUCCESS=>${repository}")
                } catch (e: Exception) {
                    notificationClearMutableData.postValue(
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
}