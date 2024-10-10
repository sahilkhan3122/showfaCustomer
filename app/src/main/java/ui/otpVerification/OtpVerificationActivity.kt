package ui.otpVerification

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.asLiveData
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityOtpVerificationBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import extension.showSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import laungage.MyDataStore
import network.NetworkAvailable
import ui.homeActivity.HomeActivity
import utils.API_CONSTANTS.LOGINOTP
import utils.API_CONSTANTS.REGISTEROTP
import utils.API_CONSTANTS.imagUri
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class OtpVerificationActivity() : BaseActivity<ActivityOtpVerificationBinding, OtpViewModel>(),
    OtpVerifyNavigator {

    override val layoutId: Int
        get() = R.layout.activity_otp_verification
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG = OtpVerificationActivity::class.java.simpleName
    }

    var loginIndex: Int? = null
    var registerIndex: Int? = null
    lateinit var myDataStore: MyDataStore

    var email = ""
    var mobilenumber = ""
    var firstname = ""
    var lastname = ""
    var lng = ""
    var lat = ""
    var profile = ""
    var dob = ""
    var gender = ""
    var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataStore = MyDataStore(this)

        if (intent != null) {
            val otp = intent.extras?.getString("OTP")
            loginIndex = intent.extras?.getInt("LOGININDEX")
            registerIndex = intent.extras?.getInt("REGISTERINDEX")
            Log.d(TAG, "OTP---->$otp")
            Log.d(TAG, "OTP---->$loginIndex")
        }
        myDataStore.getUSerFirstName.asLiveData().observe(this) {
            Log.d(TAG, "name==>$it")
            firstname = it
        }
        myDataStore.getUSerlastName.asLiveData().observe(this) {
            Log.d(TAG, "lastname==>$it")
            lastname = it

        }
        myDataStore.getEmail.asLiveData().observe(this) {
            email = it
            Log.d(TAG, "email==>$it")

        }
        myDataStore.getMobileNumber.asLiveData().observe(this) {
            mobilenumber = it
            Log.d(TAG, "email==>$it")

        }
        myDataStore.getAddress.asLiveData().observe(this) {
            address = it
            Log.d(TAG, "address==>$it")
        }
        myDataStore.getDateOfBirth.asLiveData().observe(this) {
            dob = it
            Log.d(TAG, "dob==>$it")

        }
        myDataStore.getProfile.asLiveData().observe(this) {
            imagUri=it.toUri()
            Log.d(TAG, "$it")
        }
        myDataStore.getLat.asLiveData().observe(this) {
            lat = it
            Log.d(TAG, "dob==>$it")
        }
        myDataStore.getLng.asLiveData().observe(this) {
            lng = it
            Log.d(TAG, "dob==>$it")
        }


    }

    override fun setupObservable() {
        var edtOtp = binding.edtOtp.text.toString()
        mViewModel.setNavigator(this)
        mViewModel.getRegisterObservable().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    if (it.data?.status == true) {
                        Log.d(TAG, "OTP---->$loginIndex")
                        CoroutineScope(Dispatchers.IO).launch {
                            myDataStore.setLoginUser(true)
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            myDataStore.setLoginUser(true)
                            myDataStore.setFirstNameData(it.data.data.first_name)
                            myDataStore.setLastNameData(it.data.data.last_name)
                            myDataStore.setEmailData(it.data.data.email)
                            myDataStore.setMobileData(it.data.data.mobile_no)
                            myDataStore.setUSerId(it.data.data.id)
                            myDataStore.setAddressData(it.data.data!!.address)
                            myDataStore.setLat(it.data.data.lat)
                            myDataStore.setLng(it.data.data.lng)
                            myDataStore.setXApiKey(it.data.data.xApiKey)
                            myDataStore.setProfile(it.data.data.profile_image)
                            myDataStore.setDeviceToken(it.data.data.device_token)
                            myDataStore.setDeviceType(it.data.data.device_type)


                            Log.d(TAG, "TOKEN--->${it.data.data.device_token}")
                            Log.d(TAG, "DATA--->${it.data.data}")
                            Log.d(TAG, "profile_image--->${it.data.data.profile_image}")
                            Log.d(TAG, "MobileNumber--->${it.data.data.mobile_no}")
                            Log.d(TAG, "USERId--->${it.data.data.id}")
                            Log.d(TAG, "XAPI--->${it.data.data.xApiKey}")
                            Log.d(TAG, "LAT--->${it.data.data.lat}")
                            Log.d(TAG, "LNG--->${it.data.data.lng}")
                            Log.d(TAG, "LNG--->${it.data.data.profile_image}")

                            startActivity(Intent(this@OtpVerificationActivity,HomeActivity::class.java))

                        }
                    }
                }

                Status.ERROR -> {
                    Log.d(TAG, "ErrorRegister----->${it.data?.message}")
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }

        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millisUntilFinished
                        )
                    )
                )
                binding.txtTime.text = hms
            }

            override fun onFinish() {
                binding.txtTime.visibility = View.INVISIBLE
                binding.txtResend.visibility = View.VISIBLE
            }
        }
        timer.start()

        mViewModel.getResendOtpObservable().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()

                }

                Status.ERROR -> {
                    Log.d(TAG, "ErrorOtp----->")

                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()


                }
            }
        }
    }

    override fun onNextButton() {
        var edtOtp = binding.edtOtp.text!!.toString()

        if (edtOtp.isEmpty()) {
            binding.root.showSnackBar(getString(R.string.enter_otp))

        } else if (edtOtp.length < 6) {
            binding.root.showSnackBar(getString(R.string.enter_valid_otpotp))
        } else if (NetworkAvailable.isNetworkAvailable(this)) {
            if (loginIndex == 1) {
                Log.d(TAG, "LOGINOTP==>$LOGINOTP")
                if (LOGINOTP == edtOtp) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            } else if (registerIndex == 2) {
                Log.d(TAG, "REGISTEROTP==>$REGISTEROTP")
                edtOtp = REGISTEROTP
                if (REGISTEROTP == edtOtp) {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                    mViewModel.registerApi(
                        email,
                        mobilenumber,
                        firstname,
                        lastname,
                        lat,
                        lng,
                        imagUri,
                        dob,
                        gender,
                        address
                    )
                }
            }

        }


    }

    override fun resendOtp() {
        Toast.makeText(this, "resend otp", Toast.LENGTH_SHORT).show()
        mViewModel.resendOtpApi()
    }


}