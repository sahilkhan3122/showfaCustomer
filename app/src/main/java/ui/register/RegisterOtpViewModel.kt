package ui.register

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.showfa_customer_android.R
import model.OtpVerifyModel
import model.ProfileUpdateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import ui.login.LoginViewModel
import ui.otpVerification.OtpVerifyRepository
import utils.API_CONSTANTS
import utils.API_CONSTANTS.imagUri
import utils.RequestCodeCheck
import utils.ShowStatus
import java.io.File
import javax.inject.Inject

class RegisterOtpViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    var firstname: String = ""
    var laseName: String = ""
    var email: String = ""
    var mobileNumber: String = ""
    var address: String = ""
    var dateOfBirth: String = ""
    var nationalID: String = ""
    var gender: String = getString(R.string.male)
    var referralCode: String = ""

    companion object {
        var TAG = LoginViewModel::class.java.simpleName
    }

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var otpVerifyRepository: OtpVerifyRepository

    @Inject
    lateinit var profileUpdateRepository: ProfileUpdateRepository

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    private val initRegisterObservable: MutableLiveData<ShowStatus<OtpVerifyModel>> =
        MutableLiveData()

    private val profileUpdateResponseMutableLiveDAta: MutableLiveData<ShowStatus<ProfileUpdateModel>> =
        MutableLiveData()

    fun getRegisterObservable(): LiveData<ShowStatus<OtpVerifyModel>> {
        return initRegisterObservable
    }


    fun getProfileUpdatetObservable(): LiveData<ShowStatus<ProfileUpdateModel>> {
        return profileUpdateResponseMutableLiveDAta
    }

    private fun getString(plzEnterEmail: Int): String {
        return context.resources.getString(plzEnterEmail)
    }


    fun otpVerifyApi() {

        val hashMap = HashMap<String, String>()
        hashMap["email"] = email
        hashMap["mobile_no"] = mobileNumber

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {

                    val repository = otpVerifyRepository.getOtpVerify(hashMap)
                    initRegisterObservable.postValue(requestCodeCheck.getResponse(repository))
                    Log.d(TAG, "SUCCESS=>${repository}")
                } catch (e: Exception) {

                    initRegisterObservable.postValue(
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

    fun profileUpdateAPi(id: String, image: String) {
        Log.d(TAG, "id==>$id")
        Log.d(TAG, "firstname==>$firstname")
        Log.d(TAG, "laseName==>$laseName")

        var image: MultipartBody.Part? = null
        if (imagUri != null) {
            image = getMultipartImage(imagUri!!, context)
        }
        val hashMap = HashMap<String, RequestBody>()
        hashMap["customer_id"] = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id)
        hashMap["first_name"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), firstname)
        hashMap["last_name"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), laseName)
        hashMap["profile_image"] =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),image.toString())

        viewModelScope.launch {
            try {
                var repository = profileUpdateRepository.getProfile(hashMap)
                profileUpdateResponseMutableLiveDAta.postValue(
                    requestCodeCheck.getResponse(
                        repository
                    )
                )
                Log.d(TAG, "SUCCESS=>${repository}")

            } catch (e: Exception) {
                profileUpdateResponseMutableLiveDAta.value = ShowStatus.error(null, "${e}")
                Log.d(TAG, "ERROR APi=>${e}")

            }
        }
    }
    private fun getMultipartImage(imagePath: Uri, context: Context): MultipartBody.Part? {

//        val documentImage = File(RealPathUtil.getRealPath(context, imagePath).toString())
        val documentImage = File(imagePath.toString())
        val requestImageFile =
            documentImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            API_CONSTANTS.PROFILEIMAGE,
            documentImage.name,
            requestImageFile
        )
        return null
    }
}