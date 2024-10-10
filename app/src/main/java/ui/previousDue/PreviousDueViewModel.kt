package ui.previousDue

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import model.PreviousDueModel
import kotlinx.coroutines.launch
import laungage.MyDataStore
import retrofit2.Retrofit
import ui.invoice.InvoicesViewModel
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class PreviousDueViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var previousRepository: PreviousRepository

    companion object {
        var TAG = InvoicesViewModel::class.java.simpleName
    }

    private val previousMutableLiveData: MutableLiveData<ShowStatus<PreviousDueModel>> =
        MutableLiveData()
    val previousLiveData: LiveData<ShowStatus<PreviousDueModel>> = previousMutableLiveData


    fun downloadInvoiceApiCall() {
        viewModelScope.launch {
            try {
                var repository = previousRepository.previousDueRepository()
                previousMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS")

            } catch (e: Exception) {
                previousMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERROR==>${e}")
            }
        }
    }
}