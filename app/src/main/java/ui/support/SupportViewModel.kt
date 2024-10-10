package ui.support

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import com.example.utils.Status
import model.SupportModel
import extension.showSnackBar
import kotlinx.coroutines.launch
import laungage.MyDataStore
import retrofit2.Retrofit
import ui.invoice.InvoicesViewModel
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class SupportViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {


    var support = ""
    var describeIssue = ""

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var supportRepository: SupportRepository

    companion object {
        var TAG = InvoicesViewModel::class.java.simpleName
    }

    private val supportMutableLiveData: MutableLiveData<ShowStatus<SupportModel>> =
        MutableLiveData()
    val supportLiveData: LiveData<ShowStatus<SupportModel>> = supportMutableLiveData


    fun supportApiCall() {
        val hashMap = HashMap<String, String>()
        hashMap["user_id"] = 100.toString()
        hashMap["description"] = support
        hashMap["ticket_title"] = describeIssue

        viewModelScope.launch {
            try {
                var repository = supportRepository.supportRepository(hashMap)
                supportMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS")

            } catch (e: Exception) {
                supportMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERROR==>${e}")
            }
        }
    }

    fun supportValidation(view:View):Boolean{
        if (support.isEmpty()){
            view.showSnackBar(context.getString(R.string.plz_ent_sub_nm))
            return false
        }else if (describeIssue.isEmpty()){
            view.showSnackBar(context.getString(R.string.plz_ent_blood_group))
            return false

        }
        return true
    }
}