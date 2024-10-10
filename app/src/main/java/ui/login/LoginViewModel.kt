package ui.login

import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import model.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laungage.MyDataStore
import retrofit2.Retrofit
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class LoginViewModel @Inject constructor(var context: Context) : BaseViewModel<LoginNavigator>() {

    var mobileNumber = ""
    var errorMobileNumber = ""
    lateinit var locationManger: LocationManager

    var lat = ""
    var lng = ""

    private val loginResponseObservable: MutableLiveData<ShowStatus<LoginModel>> = MutableLiveData()

    @Inject
    lateinit var retrofit: Retrofit


    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var loinRepository: LoginRepository

    companion object {
        var TAG = LoginViewModel::class.java.simpleName
    }

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck


    fun getLoginObservable(): LiveData<ShowStatus<LoginModel>> {
        return loginResponseObservable
    }

    private fun getString(plzEnterEmail: Int): String {
        return context.resources.getString(plzEnterEmail)
    }

    fun loginApi(number: String, lat: Double, lng: Double) {

        val hashMap = HashMap<String, String>()
        hashMap["username"] = number
        hashMap["lat"] = lat.toString()
        hashMap["lng"] = lng.toString()
        hashMap["device_token"] = "sadfdfs65c4dwfvwdv6edbv16efbef1b165165 "
        hashMap["device_type"] = "android"
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {

                    val repository = loinRepository.getLoginRepository(hashMap)
                    loginResponseObservable.postValue(requestCodeCheck.getResponse(repository))
                    Log.d(TAG, "SUCCESS=>${repository}")
                } catch (e: Exception) {

                    loginResponseObservable.postValue(
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

