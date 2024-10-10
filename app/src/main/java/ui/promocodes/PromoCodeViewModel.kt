package ui.promocodes

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import model.PromocodsModel
import kotlinx.coroutines.launch
import laungage.MyDataStore
import retrofit2.Retrofit
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class PromoCodeViewModel@Inject constructor(var context: Context) : BaseViewModel<Any>() {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var promoCodesRepository: PromoCodeRepository

    companion object {
        var TAG = PromoCodeViewModel::class.java.simpleName
    }

    private val promoCodesMutableLiveData: MutableLiveData<ShowStatus<PromocodsModel>> =
        MutableLiveData()
    val promoCodesLiveData: LiveData<ShowStatus<PromocodsModel>> = promoCodesMutableLiveData


    fun promoCodesApiCall() {
        viewModelScope.launch {
            try {
                val repository = promoCodesRepository.promoCodeRepository()
                promoCodesMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS")

            } catch (e: Exception) {
                promoCodesMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERROR==>${e}")
            }
        }
    }
}