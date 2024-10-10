package ui.previousDue

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityPreviousDueBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import ui.invoice.InvoiceActivity

@AndroidEntryPoint
class PreviousDueActivity() : BaseActivity<ActivityPreviousDueBinding, PreviousDueViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_previous_due
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG: String = PreviousDueActivity::class.java.simpleName
    }

    var previousDueAdapter: PreviousDueAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.PreviousDueRecyclerView.layoutManager = layoutManager
        binding.previousDuoToolbar.txtTittle.text = getString(R.string.previous_due)
        binding.previousDuoToolbar.notification.visibility=View.GONE
        binding.previousDuoToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        mViewModel.downloadInvoiceApiCall()
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)

        binding.imgLoader.loader.visibility = View.VISIBLE
        binding.imgLoader.loader.show()

        mViewModel.previousLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    var data = it.data!!.data
                    previousDueAdapter = PreviousDueAdapter(this, data) {}
                    binding.PreviousDueRecyclerView.adapter = previousDueAdapter
                    if (data.isEmpty()) {
                        binding.txtDataNotFound.visibility = View.VISIBLE
                    }
                    Log.d(TAG, "SUCCESS------>")
                }

                Status.ERROR -> {
                    Log.d(InvoiceActivity.TAG, "Error----->")
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }
    }
}