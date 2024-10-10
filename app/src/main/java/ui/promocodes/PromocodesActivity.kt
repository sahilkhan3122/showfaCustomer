package ui.promocodes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityPromocodesBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromocodesActivity() : BaseActivity<ActivityPromocodesBinding, PromoCodeViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_promocodes
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG = PromocodesActivity::class.java.simpleName
    }

    var promoCodeAdapter: PromoCodeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.promoCodesRecyclerview.layoutManager = layoutManager
        binding.promoCodesToolbar.txtTittle.text = getString(R.string.promo_codes)
        binding.promoCodesToolbar.notification.visibility = View.GONE

        onBackPressClick()
        mViewModel.promoCodesApiCall()
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.promoCodesLiveData.observe(this) {
            when (it.status) {

                Status.SUCCESS -> {
                    val data = it.data!!.data
                    promoCodeAdapter = PromoCodeAdapter(this, data) {}
                    binding.promoCodesRecyclerview.adapter = promoCodeAdapter
                    Log.d(TAG, "SUCCESS------>$data")
                }

                Status.ERROR -> {
                    Log.d(TAG, "Error----->")
                }

                Status.LOADING -> {
                    Log.d(TAG, "Loading----->")
                }
            }
        }
    }

    private fun onBackPressClick() {
        binding.promoCodesToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}