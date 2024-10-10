package ui.invoice

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import model.DownloadInvoiceModel
import kotlinx.coroutines.launch
import laungage.MyDataStore
import retrofit2.Retrofit
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class InvoicesViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var invoiceRepository: InvoiceRepository

    companion object {
        var TAG = InvoicesViewModel::class.java.simpleName
    }

    private val invoiceMutableLiveData: MutableLiveData<ShowStatus<DownloadInvoiceModel>> =
        MutableLiveData()
    val invoiceLiveData: LiveData<ShowStatus<DownloadInvoiceModel>> = invoiceMutableLiveData


    fun downloadInvoiceApiCall() {
        viewModelScope.launch {
            try {
                var repository = invoiceRepository.getInvoiceRepository()
                invoiceMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS")

            } catch (e: Exception) {
                invoiceMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERROR==>${e}")
            }
        }

    }


}