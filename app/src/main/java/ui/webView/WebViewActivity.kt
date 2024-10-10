package ui.webView

import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint
import utils.API_CONSTANTS.PRIVACYANDPOLICY
import utils.API_CONSTANTS.TERMSANDCONDITIONSET
import utils.API_CONSTANTS.webview

@AndroidEntryPoint
class WebViewActivity() : BaseActivity<ActivityWebViewBinding, WebViewViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_web_view
    override val bindingVariable: Int
        get() = BR.viewModel
    var position:Int?=null
    companion object{
        var TAG=WebViewActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webView.webViewClient =WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
        getIntentData()
        initView()
    }

    private fun getIntentData(){
        if (intent!=null){
            val termsAndCondition= intent.extras?.getInt(webview)
            val privacyAndPolicy= intent.extras?.getInt(webview)
            Log.d(TAG,"termandCondition--->$termsAndCondition")
            Log.d(TAG,"privacyAndPolicy--->$privacyAndPolicy")

            if (termsAndCondition==1){
                binding.webViewToolbar.txtTittle.text=getString(R.string.privacy_policy)
                binding.webView.loadUrl(TERMSANDCONDITIONSET)
                Log.d(TAG,"termandCondition--->$TERMSANDCONDITIONSET")

            }else if(privacyAndPolicy==2){
                binding.webViewToolbar.txtTittle.text=getString(R.string.terms_of_service)
                binding.webView.loadUrl(PRIVACYANDPOLICY)
                Log.d(TAG,"termandCondition--->$PRIVACYANDPOLICY")

            }
        }
    }
    fun initView(){
        binding.webViewToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
    override fun setupObservable() {
    }
}