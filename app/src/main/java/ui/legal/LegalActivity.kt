package ui.legal

import android.content.Intent
import android.os.Bundle
import android.view.View
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityLegalBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.webView.WebViewActivity
import utils.API_CONSTANTS.webview

@AndroidEntryPoint
class LegalActivity() : BaseActivity<ActivityLegalBinding, LegalViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_legal
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG = LegalActivity::class.java.simpleName
    }

    override fun setupObservable() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.privacyPolicyToolbar.txtTittle.text = getString(R.string.legal)
        binding.privacyPolicyToolbar.notification.visibility = View.GONE

        initView()
    }

    private fun initView() {
        binding.privacyPolicyToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.txtTermAndCondition.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(webview, 1)
            startActivity(intent)
        }
        binding.txtPrivacyPolicy.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(webview, 2)
            startActivity(intent)
        }

    }
}