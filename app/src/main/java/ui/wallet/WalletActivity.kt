package ui.wallet

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.annotation.RequiresApi
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityWalletBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import laungage.MyDataStore
import model.WalletHistoryModel
import utils.API_CONSTANTS.AMOUNT
import utils.API_CONSTANTS.MOBILENUMBER
import javax.inject.Inject

@AndroidEntryPoint
class WalletActivity() : BaseActivity<ActivityWalletBinding, WalletViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_wallet

    override val bindingVariable: Int
        get() = BR.viewModel
    var page: Int = 1
    private var walletAdapter: WalletAdapter? = null
    lateinit var walletHistoryModel: ArrayList<WalletHistoryModel.Data>
    var id: String = 100.toString()
    var amount: String = ""
    var userId: String = ""
    var userType: String = ""
    var number: String? = ""
    var position: Int = 0
    var currentItem = ""
    var totalItem = ""
    var scrollItem = ""

    var isScrolling: Boolean? = false

    @Inject
    lateinit var myDataStore: MyDataStore

    companion object {
        var TAG = WalletActivity::class.java.simpleName
    }

    @SuppressLint("NewApi", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        walletHistoryModel = arrayListOf()
        binding.imgLoader.loader.visibility = View.VISIBLE
        binding.imgLoader.loader.show()
        page?.let { mViewModel.walletApiCall(id, it) }
        Log.d(TAG, "Page====>$page")

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.walletHistoryRecyclerView.layoutManager = layoutManager



        binding.walletHistoryRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    Handler().postDelayed({
                        isScrolling = true
                        val a = page++
                        Log.d(TAG, "Page===>$a")
                        mViewModel.walletApiCall(id, a)

                    }, 2000)

                    binding.progressbar.visibility = View.VISIBLE
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling == true) {
                    binding.progressbar.visibility = View.GONE
                }
            }

        })

        walletAdapter?.notifyDataSetChanged()
        binding.imgAddMoney.setOnClickListener {
            DialogShow(this).bottomSheet(
                txtAddress = getString(R.string.add_mony),
                true,
                this,
                {
                    mViewModel.walletAddMoneyApiCall(id = id!!, amount)
                }, {},
                "imgAddMoney"
            )
            Log.d(TAG, "amount===>$amount")
        }
        binding.imgSentMoney.setOnClickListener {

            myDataStore.getMobileNumber.asLiveData().observe(this) { numberSet ->
                number = numberSet
            }

            Log.d(TAG, "GETNUMBER====>$MOBILENUMBER")
            Log.d(TAG, "GETNUMBER====>$amount")
            Log.d(TAG, "GETNUMBER====>$userId")
            Log.d(TAG, "GETNUMBER====>$userType")

            DialogShow(this).bottomSheet(
                txtAddress = getString(R.string.sent_money),
                true,
                this,
                {
                    number?.let { it1 ->
                        mViewModel.checkMobileNumberApiCall(
                            mobile = MOBILENUMBER,
                            userType = userType,
                            amount = AMOUNT,
                            senderId = userId
                        )
                    }
                }, {},
                "imgSentMoney"
            )

        }
        binding.imgActiveWallet.setOnClickListener {
            dialog.DialogShow(this).networkDialog(
                txtTitle = View.GONE,
                buttonClose = View.GONE,
                txtmessage = resources.getString(R.string.physicalAddress),
                txtConnection = View.GONE,
                null
            )

        }
    }

    private fun initView() {
        binding.walletToolbar.txtTittle.text = getString(R.string.wallet)
        binding.walletToolbar.notification.visibility = View.GONE

        binding.walletToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupObservable() {
        mViewModel.setNavigator(this)
        binding.progressbar.visibility = View.INVISIBLE
        mViewModel.walletLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    val data = it.data?.data
                    walletHistoryModel = it.data?.data!!

                    amount = it.data.data[position].amount
                    userId = it.data.data[position].user_id
                    userType = it.data.data[position].user_type


                    val layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.walletHistoryRecyclerView.layoutManager = layoutManager


                    walletAdapter = WalletAdapter(this, data!!) { position ->
                        this@WalletActivity.position = position
                    }
                    binding.walletHistoryRecyclerView.adapter = walletAdapter

                    if (it.data.is_saving_wallet == "0") {
                        binding.imgActiveWallet.visibility = View.VISIBLE
                        binding.txtActiveYourWallet.visibility = View.VISIBLE

                        binding.imgAddMoney.visibility = View.INVISIBLE
                        binding.txtAddMoney.visibility = View.INVISIBLE

                        binding.imgSentMoney.visibility = View.INVISIBLE
                        binding.txtSentMoney.visibility = View.INVISIBLE
                    } else {
                        binding.imgActiveWallet.visibility = View.INVISIBLE
                        binding.txtActiveYourWallet.visibility = View.INVISIBLE

                        binding.imgAddMoney.visibility = View.VISIBLE
                        binding.txtAddMoney.visibility = View.VISIBLE

                        binding.imgSentMoney.visibility = View.VISIBLE
                        binding.txtSentMoney.visibility = View.VISIBLE
                    }

                    Log.d(TAG, "SUCCESS------>$data")
                    walletAdapter!!.notifyDataSetChanged()
                }

                Status.ERROR -> {
                    Log.d(TAG, "Error----->")
                }

                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }

        mViewModel.walletAddMoneyLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                }

                Status.ERROR -> {
                    Log.d(TAG, "Error----->")
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }

        mViewModel.setNavigator(this)

        mViewModel.sendMoneyWalletLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                }

                Status.ERROR -> {
                    Log.d(TAG, "Error----->")
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                }

                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }

        mViewModel.checkMobileNumberLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    Log.d(TAG, "data==>${it.data}")
                    if (it.data!!.receiptType == 2) {
                        DialogShow(this).bottomSheet(
                            txtAddress = getString(R.string.ConfirmPAyment),
                            true,
                            this,
                            null,
                            null,
                            "checkMobileNumber"
                        )
                    } else {

                        DialogShow(this).bottomSheet(
                            txtAddress = getString(R.string.ConfirmPAyment),
                            true,
                            this,
                            null,
                            null,
                            "ConfirmPAyment", AMOUNT, MOBILENUMBER, null
                        )
                    }
                    Log.d(TAG, "SUCCESS------>${it.data.totalReceipt}")
                    Log.d(TAG, "SUCCESS------>${it.data.receiptType}")
                }

                Status.ERROR -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    Log.d(TAG, "Error----->")
                }

                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }
            }
        }

    }


}