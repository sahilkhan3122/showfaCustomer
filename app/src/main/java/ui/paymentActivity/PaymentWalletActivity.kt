package ui.paymentActivity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityPaymentWalletBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentWalletActivity() : BaseActivity<ActivityPaymentWalletBinding, PaymentViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_payment_wallet
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutImageAndName()
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        binding.paymentToolbar.notification.visibility = View.INVISIBLE
        binding.paymentToolbar.txtTittle.text = resources.getString(R.string.payment)
        binding.paymentToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setLayoutImageAndName() {
        binding.txtPaymentMPesa.imgPaymentWallet.setImageResource(R.drawable.ic_payment_m_pesa)
        binding.txtPaymentMPesa.txtPaymentName.text = resources.getString(R.string.MPesa)

        binding.txtPaymentCash.imgPaymentWallet.setImageResource(R.drawable.ic_wallet_money)
        binding.txtPaymentCash.txtPaymentName.text = resources.getString(R.string.wallet)

        binding.txtPaymentCorporateBooking.imgPaymentWallet.setImageResource(R.drawable.ic_wallet_corporate_booking)
        binding.txtPaymentCorporateBooking.imgPaymentWallet.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.theme_color
            ), PorterDuff.Mode.SRC_IN
        )

        binding.txtPaymentCorporateBooking.txtPaymentName.text =
            resources.getString(R.string.corporeateBooking)

    }

    override fun setupObservable() {

    }

}