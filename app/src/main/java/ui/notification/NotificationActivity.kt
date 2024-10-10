package ui.notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityNotificationBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import laungage.MyDataStore
import javax.inject.Inject


@AndroidEntryPoint
class NotificationActivity() : BaseActivity<ActivityNotificationBinding, NotificationViewModel>() {

    companion object {
        var TAG: String = NotificationActivity::class.java.simpleName
    }

    override val layoutId: Int
        get() = R.layout.activity_notification
    override val bindingVariable: Int
        get() = BR.viewModel

    @Inject
    lateinit var myDataStore: MyDataStore

    @SuppressLint("DiscouragedApi", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clickEventToolbar()

        myDataStore.getUSerId.asLiveData().observe(this) {
            Log.d(TAG, "id==>$it")
            mViewModel.notificationApiCall(it)
        }

    }

    private fun clickEventToolbar() {
        binding.notificationToolbar.txtTittle.text = getString(R.string.notification)
        binding.notificationToolbar.notification.visibility = View.INVISIBLE
        binding.notificationToolbar.txtClearAll.visibility = View.VISIBLE
        binding.notificationToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.notificationToolbar.txtClearAll.setOnClickListener {
            myDataStore.getUSerId.asLiveData().observe(this) {
                Log.d(TAG, "id==>$it")
                mViewModel.notificationClearApiCall(it)
            }
        }

    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.notificationLiveData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data?.data
                    val layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val notificationAdapter = it.data?.let { it1 ->
                        NotificationAdapter(
                            this,
                            it1.data
                        ) {}
                    }
                    binding.notificationRecycler.layoutManager = layoutManager
                    binding.notificationRecycler.adapter = notificationAdapter
                    Log.d(TAG, "Success==>${data}")
                    if (data!!.isEmpty()) {
                        binding.txtNotDataFound.visibility = View.VISIBLE
                    }
                }

                Status.ERROR -> {
                    Log.d(TAG, "ERROR")
                }

                Status.LOADING -> {
                    Log.d(TAG, "LOADING")

                }
            }
        }
        mViewModel.notificationClearLiveData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!
                            .status){
                        binding.notificationRecycler.visibility=View.GONE
                        binding.txtNotDataFound.visibility=View.VISIBLE
                    }else{

                    }
                }
                Status.ERROR -> {
                    Log.d(TAG, "ERROR")
                }

                Status.LOADING -> {
                    Log.d(TAG, "LOADING")

                }
            }
        }
    }
}

