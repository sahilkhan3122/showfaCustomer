package ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityLoginBinding
import com.example.utils.Status
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import extension.showSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import laungage.MyDataStore
import network.NetworkAvailable
import ui.register.RegisterActivity
import utils.API_CONSTANTS.LOGINOTP
import utils.API_CONSTANTS.MOBILENUMBER
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity() : BaseActivity<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {

    override val layoutId: Int
        get() = R.layout.activity_login
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG: String = LoginActivity::class.java.simpleName
    }

    lateinit var launcher: ActivityResultLauncher<String>

    lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    var signInIntent: Intent? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocationLat: Double? = null
    var currentLocationLng: Double? = null

    @Inject
    lateinit var myDataStore: MyDataStore
    lateinit var networkConnectionDialog: DialogShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectionDialog = DialogShow(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        socialMedialSignInButtonClick()
        clickNextButton()
        onRegisterTextCLick()
        activityLauncher()

        binding.imgGoogle.setOnClickListener {
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun activityLauncher() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getCurrentLocation()
                } else {
                    binding.root.showSnackBar(getString(R.string.Please_location_on))
                }
            }
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(result.data)
                Log.d(TAG, "Result==>${result.data}")
            }
        }
    }

    private fun onRegisterTextCLick() {
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun socialMedialSignInButtonClick() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        signInIntent = googleSignInClient.signInIntent

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            Log.d(TAG, "result==>$task")
            task.addOnCompleteListener {
                if (task.isSuccessful) handleSignData(result.data)
            }
        }

        binding.imgGoogle.setOnClickListener {
            if (!NetworkAvailable.isNetworkAvailable(this)) {
                binding.root.showSnackBar(getString(R.string.plz_check_internet_connection))
            } else {
                socialMediaGoogleLogin()
            }
        }
    }

    private fun handleSignData(data: Intent?) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnCompleteListener {
                Log.e(
                    "isSuccessful",
                    "data==>${it.result.displayName}, ${it.result.givenName}, ${it.result.familyName}"
                )
                if (it.isSuccessful) {


                } else {
                    // authentication failed
                    Log.e("exception", "data==>${it.exception}")
                }
            }

    }


    private fun handleSignInResult(completedTask: Intent?) {

        GoogleSignIn.getSignedInAccountFromIntent(completedTask).addOnCompleteListener {
            Log.e(
                "isSuccessful",
                "${it.result.displayName}, ${it.result.givenName}, ${it.result.familyName}"
            )
            if (it.isSuccessful) {

            } else {
                Log.e("exception", "${it.exception}")
            }
        }
    }

    fun socialMediaGoogleLogin() {
        googleSignInLauncher.launch(signInIntent)
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)

        mViewModel.getLoginObservable().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()

                    if (it.data?.status == true) {

                        DialogShow(this).networkDialog(
                            txtTitle = View.GONE,
                            buttonClose = View.GONE,
                            it.data.message,
                            txtConnection = View.GONE,
                            1
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            myDataStore.setLoginUser(true)
                            myDataStore.setFirstNameData(it.data.data.firstName)
                            myDataStore.setLastNameData(it.data.data.last_name)
                            myDataStore.setUSerId(it.data.data.id)
                            myDataStore.setEmailData(it.data.data.email)
                            myDataStore.setMobileData(it.data.data.mobileNo)
                            myDataStore.setAddressData(it.data.data.address)
                            myDataStore.setDateOfBirthData(it.data.data.dob)
                            myDataStore.setNationalData(it.data.data.identifyNumber)
                            myDataStore.setReferralData(it.data.data.referralCode)
                            myDataStore.setGenderData(it.data.data.gender)
                            myDataStore.setProfile(it.data.data.profileImage)
                            myDataStore.setXApiKey(it.data.data.xApiKey)
                            it.data.otp?.let { it1 -> myDataStore.setUSerOtp(it1) }
                        }
                        LOGINOTP = it.data.otp.toString()
                        Log.d(TAG, "LOGINOTP==>$LOGINOTP")
                        Log.d(TAG, "KEY==>${it.data.data.xApiKey}")
                        Log.d(TAG, "KEY==>${it.data.otp}")
                        Log.d(TAG, "USERID==>${it.data.data.id}")
                    } else {
                        binding.root.showSnackBar(getString(R.string.this_number_Not_register))
                    }
                }

                Status.ERROR -> {
                    Log.d(TAG, "error")
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }
    }

    private fun clickNextButton() {
        binding.btnNext.setOnClickListener {
            val edtNumber = binding.edtNumber.text!!.toString()
            if (edtNumber.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_numbe))
            } else if (!NetworkAvailable.isNetworkAvailable(this)) {
                binding.root.showSnackBar(getString(R.string.internet_connection))
            } else {
                binding.imgLoader.loader.visibility = View.VISIBLE
                binding.imgLoader.loader.show()

                Log.d(TAG, "CURRUNTLAT---->${currentLocationLat}")
                Log.d(TAG, "CURRUNTLNG---->${currentLocationLng}")

                currentLocationLat?.let { it1 ->
                    currentLocationLng?.let { it2 ->
                        mViewModel.loginApi(
                            edtNumber,
                            it1, it2
                        )
                    }
                }
                MOBILENUMBER = edtNumber
            }

        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                val location = task.result
                Log.d("LOCATION", "location==>$location")
                if (location != null) {
                    currentLocationLat = location.latitude
                    currentLocationLng = location.longitude
                    Log.d(TAG, "LAT---->${location.latitude}")
                    Log.d(TAG, "LNG---->${location.latitude}")
                }
            }
        }
    }

    override fun onNext() {
    }

    override fun onRegisterText() {
    }

}


