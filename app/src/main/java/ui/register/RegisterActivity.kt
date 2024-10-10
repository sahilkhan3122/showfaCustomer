package ui.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.asLiveData
import base.BaseActivity
import com.bumptech.glide.Glide
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityRegisterBinding
import com.example.utils.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import extension.getDateFormat
import extension.hideSoftKeyboard
import extension.isValidEmail
import extension.showSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import laungage.MyDataStore
import network.NetworkAvailable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ui.otpVerification.OtpVerificationActivity
import utils.API_CONSTANTS
import utils.API_CONSTANTS.FIRSTNAME
import utils.API_CONSTANTS.REGISTEROTP
import utils.API_CONSTANTS.URL
import utils.API_CONSTANTS.imagUri
import utils.RealPathUtil
import java.io.File
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity() : BaseActivity<ActivityRegisterBinding, RegisterOtpViewModel>(),
    RegisterNavigator {

    override val layoutId: Int
        get() = R.layout.activity_register
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG: String = RegisterActivity::class.java.simpleName
    }

    @Inject
    lateinit var myDataStore: MyDataStore
    lateinit var launchResult: ActivityResultLauncher<String>
    private var cameraImagePath: String? = null
    var gallery = false
    var index: Int? = null
    var id: String = ""

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentDataOtpSent()
        userProfilePluseImageClick()
        btnNextClickEvent()
        binding.edtDateOfBirth.setOnClickListener {
            dateOfBirthCalenderOpen()
            hideSoftKeyboard(this)
        }

        val takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                Glide.with(this).load(cameraImagePath)
                    .placeholder(R.drawable.ic_launcher_background).into(binding.imgUSer)
                imagUri= cameraImagePath?.toUri()
                Log.d(TAG, "success==>${imagUri}")
            }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imgUSer.setImageURI(uri)
                    Log.d(TAG, "==>$uri")
                    imagUri= uri
                    Log.d(TAG, "uri==>${imagUri}")

                }
            }

        launchResult =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    if (!gallery) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        takePicture.launch(generateImageUrl())
                    }
                } else {
                    val showRationale =
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)

                    if (!showRationale) {
                        val deniedDialog = AlertDialog.Builder(this)

                        deniedDialog.setTitle(R.string.permission_denied)
                        deniedDialog.setMessage(R.string.permission_setting)
                        deniedDialog.setPositiveButton(
                            R.string.setting
                        ) { _, _ ->
                            showPermissionDeniedDialog()
                        }
                        deniedDialog.setNegativeButton(R.string.cancel, null)
                        deniedDialog.show()
                    }
                }

            }
    }

    @SuppressLint("ResourceType")
    private fun getIntentDataOtpSent() {
        if (intent != null) {
            index = intent.extras?.getInt("index")
            Log.d(TAG, "INDEX--->$index")
            if (index == 1) {
                myDataStore.getUSerFirstName.asLiveData().observe(this) {
                    Log.d(TAG, "name==>$it")
                    binding.edtFirstName.setText(it)
                }
                myDataStore.getUSerlastName.asLiveData().observe(this) {
                    Log.d(TAG, "lastname==>$it")
                    binding.edtLastName.setText(it)

                }
                myDataStore.getEmail.asLiveData().observe(this) {
                    binding.edtEmail.setText(it)

                }
                myDataStore.getMobileNumber.asLiveData().observe(this) {
                    binding.edtNumber.setText(it)

                }
                myDataStore.getAddress.asLiveData().observe(this) {
                    binding.edtAddress.setText(it)

                }
                myDataStore.getDateOfBirth.asLiveData().observe(this) {
                    binding.edtDateOfBirth.setText(it)

                }
                myDataStore.getNationalId.asLiveData().observe(this) {
                    binding.edtNationalId.setText(it)

                }
                myDataStore.getProfile.asLiveData().observe(this) {
                    imagUri = it.toUri()
                    Glide.with(this)
                        .load(API_CONSTANTS.URL + imagUri)
                        .into(binding.imgUSer)
                    Log.d(TAG, "PROFILEIMAGE===>$imagUri")

                }
                myDataStore.getReferralCode.asLiveData().observe(this) {
                    binding.edtReferralCode.setText(it)

                }
                myDataStore.getUSerId.asLiveData().observe(this) {
                    id = it

                }
                binding.btnNext.text = "Save"
                binding.welcomeToSwofa.visibility = View.GONE
                binding.imgBg.visibility = View.GONE
                binding.editProfilToolbar.root.visibility = View.VISIBLE
                binding.editProfilToolbar.txtTittle.text = getString(R.string.edit_Profile)
                binding.editProfilToolbar.imgHeader.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }

            }
        }
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)

        mViewModel.getRegisterObservable().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    CoroutineScope(Dispatchers.IO).launch {
                        myDataStore.setLoginUser(true)
                        myDataStore.setFirstNameData(mViewModel.firstname)
                        myDataStore.setLastNameData(mViewModel.laseName)
                        myDataStore.setEmailData(mViewModel.email)
                        myDataStore.setMobileData(mViewModel.mobileNumber)
                        myDataStore.setAddressData(mViewModel.address)
                        myDataStore.setDateOfBirthData(mViewModel.dateOfBirth)
                        myDataStore.setNationalData(mViewModel.nationalID)
                        myDataStore.setReferralData(mViewModel.referralCode)
                        myDataStore.setGenderData(mViewModel.gender)
                        Log.d(TAG, "image====>${imagUri}")

                        myDataStore.setProfile(imagUri.toString())
                        it.data?.otp?.let { it1 -> myDataStore.setUSerOtp(it1) }
                        Log.d(TAG, "REgisterotp====>${it.data?.otp}")
                        REGISTEROTP = it.data?.otp.toString()


                    }
                    val intent = Intent(this, OtpVerificationActivity::class.java)
                    intent.putExtra("REGISTERINDEX", 2)
                    startActivity(intent)
                }

                Status.ERROR -> {
                    Log.d(TAG, "Error")
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }

        mViewModel.getProfileUpdatetObservable().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d(TAG, "xapikey===>${it.data?.data?.xApiKey}")
                    Log.d(TAG, "DATA===>${it.data?.data}")
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    CoroutineScope(Dispatchers.IO).launch {
                        myDataStore.setFirstNameData(it.data?.data!!.first_name)
                        myDataStore.setLastNameData(it.data.data.last_name)
                        myDataStore.setEmailData(it.data.data.email)
                        myDataStore.setUSerId(it.data.data.id)
                        myDataStore.setMobileData(it.data.data.mobile_no)
                        myDataStore.setAddressData(it.data.data.address)
                        myDataStore.setDateOfBirthData(it.data.data.dob)
                        myDataStore.setNationalData(it.data.data.identify_number)
                        myDataStore.setReferralData(it.data.data.referral_code)
                        myDataStore.setGenderData(it.data.data.gender)
                        myDataStore.setProfile(it.data.data.profile_image)
                        myDataStore.setXApiKey(it.data.data.xApiKey)
                        Log.d(TAG, "REGISTERUSERID===>${it.data.data.id}")
                        Log.d(TAG, "setProfile===>${it.data.data.profile_image}")
                        Log.d(TAG, "setProfile===>${it.data.data.profile_image}")
                        Log.d(TAG, "firstname===>${it.data?.data!!.first_name}")

                    }
                    it.data?.let { it1 ->
                        DialogShow(this).networkDialog(
                            txtTitle = View.VISIBLE,
                            buttonClose = View.VISIBLE,
                            it1.message,
                            txtConnection = View.GONE,
                            2
                        )
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

    private fun btnNextClickEvent() {
        binding.btnNext.setOnClickListener {
            if (binding.edtFirstName.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_fnm))

            } else if (binding.edtLastName.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_lnm))

            } else if (binding.edtEmail.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_email))

            } else if (!isValidEmail(binding.edtEmail.text.toString().trim())) {
                binding.root.showSnackBar(getString(R.string.valide_email))

            } else if (binding.edtNumber.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_numbe))

            } else if (binding.edtAddress.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_address))

            } else if (binding.edtDateOfBirth.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.select_dob))

            } else if (binding.edtNationalId.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.national_id))

            } else if (binding.edtReferralCode.text!!.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.enter_referral_code))

            } else if (NetworkAvailable.isNetworkAvailable(this)) {
                Log.d(TAG, "index==>$index")
                if (index == 1) {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                    Log.d(TAG, "id==>$id")
                    Log.d(TAG, "id==>$imagUri")
                    mViewModel.profileUpdateAPi(id, imagUri.toString())
                } else {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                    mViewModel.otpVerifyApi()
                }
            }

        }

    }

    private fun userProfilePluseImageClick() {
        binding.imgPlus.setOnClickListener {
            val bottomSheet = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val view = layoutInflater.inflate(R.layout.camera_gallery_bottomsheet, null)
            val imgClose = view.findViewById<ImageView>(R.id.imgClose)
            val imgCamera = view.findViewById<ImageView>(R.id.imgCamera)
            val imgGallery = view.findViewById<ImageView>(R.id.imgGallery)
            imgClose.setOnClickListener {
                bottomSheet.dismiss()
            }
            imgCamera.setOnClickListener {
                gallery = true
                launchResult.launch(Manifest.permission.CAMERA)
                bottomSheet.dismiss()
            }

            imgGallery.setOnClickListener {
                gallery = false
                launchResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                bottomSheet.dismiss()
            }
            bottomSheet.setContentView(view)
            bottomSheet.show()
        }
    }

    private fun showPermissionDeniedDialog() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package", this.packageName, null
        )
        intent.data = uri
        startActivity(intent)
    }

    @SuppressLint("ResourceAsColor")
    override fun onRegister() {
    }

    override fun signIn() {
        Toast.makeText(this, "selected", Toast.LENGTH_SHORT).show()
    }

    private fun generateImageUrl(): Uri {
        val string = "${this.filesDir}/Image.jpg"
        cameraImagePath = string
        return FileProvider.getUriForFile(
            this,
            "com.example.showfa_customer_android.provider",
            File(string)
        )
    }

    @SuppressLint("SuspiciousIndentation")
    private fun dateOfBirthCalenderOpen() {
        val calender = Calendar.getInstance()
        val date =
            OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateOfBirth = getDateFormat(calender.time)
                binding.edtDateOfBirth.setText(dateOfBirth)
            }
        val dpd = DatePickerDialog(
            this, android.app.AlertDialog.THEME_HOLO_LIGHT,
            date, calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )

        val dp = dpd.datePicker
        dp.maxDate = System.currentTimeMillis()
        dpd.show()
    }


}