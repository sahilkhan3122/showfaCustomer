package ui.support

import android.os.Bundle
import android.util.Log
import android.view.View
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivitySupportBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import ui.wallet.WalletActivity

@AndroidEntryPoint
class SupportActivity() : BaseActivity<ActivitySupportBinding, SupportViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_support
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        binding.btnDone.setOnClickListener {
            if (mViewModel.supportValidation(binding.root)) {
                binding.imgLoader.loader.visibility = View.VISIBLE
                binding.imgLoader.loader.show()
                mViewModel.supportApiCall()
            }
        }
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.supportLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    it.data?.let { it1 ->
                        DialogShow(this).networkDialog(
                            txtTitle = View.VISIBLE,
                            buttonClose = View.VISIBLE,
                            txtmessage = it1.message,
                            txtConnection = View.GONE,
                            0)
                    }
                    binding.edtSupport.text?.clear()
                    binding.edtDescribeIssue.text?.clear()
                }

                Status.ERROR -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    Log.d(WalletActivity.TAG, "Error----->")
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }
    }

    private fun initView() {
        binding.supportToolbar.txtTittle.text = getString(R.string.support)
        binding.supportToolbar.notification.visibility=View.GONE

        binding.supportToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}