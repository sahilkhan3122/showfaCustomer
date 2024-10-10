package ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.asLiveData
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivitySplashScreenBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import laungage.MyDataStore
import network.NetworkAvailable
import ui.homeActivity.HomeActivity
import ui.login.LoginActivity
import utils.API_CONSTANTS.PRIVACYANDPOLICY
import utils.API_CONSTANTS.TERMSANDCONDITIONSET
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity() : BaseActivity<ActivitySplashScreenBinding, SplashViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_splash_screen
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG: String = SplashScreenActivity::class.java.simpleName
    }

    @Inject
    lateinit var myDataStore: MyDataStore
    lateinit var networkConnectionDialog: DialogShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        networkConnectionDialog = DialogShow(this)

        if (NetworkAvailable.isNetworkAvailable(this)) {
            myDataStore.getUSerId.asLiveData().observe(this){
                mViewModel.initApi()
            }

        } else {
            networkConnectionDialog.networkDialog(
                txtTitle = View.VISIBLE,
                buttonClose = View.VISIBLE,
                txtmessage = getString(R.string.please_check_internet),
                txtConnection = View.VISIBLE,
                0
            )
        }
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.getInitObservable().observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d(TAG,"Success==>${it.data!!}")
                    if (it.data!!.status) {
                        val handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed({
                            Log.d(TAG,"datastore==>${it}")
                            myDataStore.getLoginUser.asLiveData().observe(this) {
                                if (it == true) {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                } else {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                            }
                        }, 3000)
                        TERMSANDCONDITIONSET = it.data!!.terms_condition_url
                        PRIVACYANDPOLICY = it.data.privacy_policy_url
                    }
                }

                Status.ERROR -> {
                    Log.d(TAG,"ERROR==>")

                    DialogShow(this).sessionExpiredDialogShow(
                        this,
                        getString(R.string.session_expire),
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        1,
                        myDataStore
                    )
                }

                Status.LOADING -> {
                    Log.d(TAG,"loading====>")
                }

            }
        }
    }

}
