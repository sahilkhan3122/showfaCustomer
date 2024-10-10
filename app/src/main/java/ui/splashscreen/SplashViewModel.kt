package ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import model.SplashModel
import utils.RequestCodeCheck
import utils.ShowStatus
import kotlinx.coroutines.launch
import network.ApiInterface
import retrofit2.Retrofit
import ui.splashscreen.SplashScreenActivity.Companion.TAG
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak") var context: Context,
    var apiInterface: ApiInterface
) :
    BaseViewModel<Any>() {

    private val initResponseObservable: MutableLiveData<ShowStatus<SplashModel>> = MutableLiveData()

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var splashRepository: SplashscreenRepository

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck


    fun getInitObservable(): LiveData<ShowStatus<SplashModel>> {
        return initResponseObservable
    }


    fun initApi() {
        viewModelScope.launch {
            try {
                val repository = splashRepository.getRepository()
                initResponseObservable.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS=>${repository}")
            } catch (e: Exception) {

                initResponseObservable.postValue(
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