package ui.otpVerification

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import model.RegisterModel
import model.ResendOTPModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import laungage.MyDataStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import ui.register.RegisterRepository
import utils.API_CONSTANTS.imagUri
import utils.RealPathUtil
import utils.RequestCodeCheck
import utils.ShowStatus
import java.io.File
import javax.inject.Inject

class OtpViewModel @Inject constructor(var context: Context) :
    BaseViewModel<OtpVerifyNavigator>() {

    private val registerResponseObservable: MutableLiveData<ShowStatus<RegisterModel>> =
        MutableLiveData()
    private val resendOtpResponseObservable: MutableLiveData<ShowStatus<ResendOTPModel>> =
        MutableLiveData()


    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var registerRepository: RegisterRepository

    @Inject
    lateinit var resendRepository: ResendRepository


    companion object {
        var TAG = OtpViewModel::class.java.simpleName
    }

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var myDataStore: MyDataStore


    fun getRegisterObservable(): LiveData<ShowStatus<RegisterModel>> {
        return registerResponseObservable
    }

    fun getResendOtpObservable(): LiveData<ShowStatus<ResendOTPModel>> {
        return resendOtpResponseObservable
    }

    fun registerApi(
        email: String,
        mobilenumber: String,
        firstname: String,
        lastname: String,
        lat: String,
        lng: String,
        profile: Uri?,
        dob: String,
        gender: String,
        address: String
    ) {
        var image: MultipartBody.Part?=null
        if (imagUri != null) {
            Log.d("IMAGEURI","image===>$imagUri")
            image = getMultipartImage(imagUri!!, context)
        }


        val hashMap = HashMap<String, RequestBody>()
        hashMap["email"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        hashMap["mobile_no"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            mobilenumber
        )
        hashMap["first_name"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), firstname)
        hashMap["last_name"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lastname)
        hashMap["lat"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lat)
        hashMap["lng"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lng)
        hashMap["device_token"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            "sadfdfs65c4dwfvwdv6edbv16efbef1b165165"
        )
        hashMap["device_type"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "android")
        /*hashMap["profile_image"] = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            image.toString()
        )*/
        hashMap["dob"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), dob)
        hashMap["gender"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), gender)
        hashMap["address"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address)

        Log.d("hashmap", "hashmap===>$image")
        Log.d("hashmap", "hashmap===>$imagUri")
        Log.d("hashmap", "hashmap===>$profile")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val repository =  registerRepository.getRegisterRepository(hashMap,image!!)
                    registerResponseObservable.postValue(requestCodeCheck.getResponse(repository))
                    Log.d(TAG, "SUCCESS=>${repository}")
                } catch (e: Exception) {

                    registerResponseObservable.postValue(
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

    fun resendOtpApi() {
        val hashMap = HashMap<String, String>()
        hashMap["mobile_no"] = "9328452118"
//        hashMap["email"] =email
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val resendRepository = resendRepository.setResendOtpRepository(hashMap)
                    resendOtpResponseObservable.postValue(
                        requestCodeCheck.getResponse(
                            resendRepository
                        )
                    )
                } catch (e: Exception) {
                    resendOtpResponseObservable.value =
                        ShowStatus.error(null, context.getString(R.string.wrong))
                }

            }
        }
    }

    private fun getMultipartImage(imagePath: Uri, context: Context): MultipartBody.Part? {

      val documentImage = File(RealPathUtil.getRealPath(context, imagePath).toString())
//       val documentImage = File(imagePath.toString())
        val requestImageFile =
            documentImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        Log.d(TAG, "url==>${documentImage.name}")
        return MultipartBody.Part.createFormData(
            "profile_image",
            documentImage.name,
            requestImageFile
        )
        return null
    }
}