package ui.wallet

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import model.WalletHistoryModel
import kotlinx.coroutines.launch
import laungage.MyDataStore
import model.CheckMobileNumberCustomerDriverModel
import model.SendWalletMoneyModel
import model.WalletAddMoneyModel
import retrofit2.Retrofit
import ui.invoice.InvoicesViewModel
import utils.RequestCodeCheck
import utils.ShowStatus
import javax.inject.Inject

class WalletViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    companion object {
        var TAG = InvoicesViewModel::class.java.simpleName
    }



    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var walletHistoryRepository: WalletHistoryRepository


    private val walletMutableLiveData: MutableLiveData<ShowStatus<WalletHistoryModel>> =
        MutableLiveData()
    var walletLiveData: LiveData<ShowStatus<WalletHistoryModel>> = walletMutableLiveData

    @Inject
    lateinit var walletAddMoneyRepository: WalletAddMoneyRepository

    private val walletAddMoneyMutableLiveData: MutableLiveData<ShowStatus<WalletAddMoneyModel>> =
        MutableLiveData()
    val walletAddMoneyLiveData: LiveData<ShowStatus<WalletAddMoneyModel>> =
        walletAddMoneyMutableLiveData

    @Inject
    lateinit var sendMoneyWalletRepository: SendMoneyWalletRepository

    private val sendMoneyWalletMutableLiveData: MutableLiveData<ShowStatus<SendWalletMoneyModel>> =
        MutableLiveData()
    var sendMoneyWalletLiveData: LiveData<ShowStatus<SendWalletMoneyModel>> =
        sendMoneyWalletMutableLiveData


    @Inject
    lateinit var checkMobileNumberRepository: CheckMobileNumberRepository

    private val checkMobileNumberMutableLiveData: MutableLiveData<ShowStatus<CheckMobileNumberCustomerDriverModel>> =
        MutableLiveData()
    var  checkMobileNumberLiveData: LiveData<ShowStatus<CheckMobileNumberCustomerDriverModel>> = checkMobileNumberMutableLiveData


    fun walletApiCall(id: String,page:Int) {
        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap["customer_id"] =id
        hashMap["page"] = page.toString()


        viewModelScope.launch {
            try {
                val repository = walletHistoryRepository.walletRepository(hashMap)
                walletMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESS")

            } catch (e: Exception) {
                walletMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERROR==>${e}")
            }
        }
    }

    fun walletAddMoneyApiCall(id: String, amount: String) {

        val hashMap = HashMap<String, String>()
        hashMap["customer_id"] = id
        hashMap["payment_type"] = "jambopay"
        hashMap["amount"] = amount

        viewModelScope.launch {

            try {
                val repository = walletAddMoneyRepository.walletAddMoneyRepository(hashMap)
                walletAddMoneyMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESSSApi==>")

            } catch (e: Exception) {
                walletAddMoneyMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERRORAPi==>${e}")
            }
        }


    }

    fun walletSendMoneyApiCall(mobile: String, userType: String, amount: String, senderId: String) {

        val hashMap = HashMap<String, String>()
        hashMap["mobile_no"] = mobile
        hashMap["user_type"] = userType
        hashMap["amount"] = amount
        hashMap["sender_id"] = senderId

        Log.d(TAG, "mobile_no==>$mobile")
        Log.d(TAG, "user_type==>$userType")
        Log.d(TAG, "amount==>$amount")
        Log.d(TAG, "sender_id==>$senderId")
        viewModelScope.launch {

            try {
                val repository = sendMoneyWalletRepository.walletRepository(hashMap)
                sendMoneyWalletMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESSSApi==>")

            } catch (e: Exception) {
                sendMoneyWalletMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERRORAPi==>${e}")
            }
        }


    }

    fun checkMobileNumberApiCall(mobile: String, userType: String, amount: String, senderId: String){
        val hashMap = HashMap<String, String>()
        hashMap["mobile_no"] = mobile
        hashMap["user_type"] = userType
        hashMap["amount"] = amount
        hashMap["sender_id"] = senderId

        Log.d(TAG, "mobile_no==>$mobile")
        Log.d(TAG, "user_type==>$userType")
        Log.d(TAG, "amount==>$amount")
        Log.d(TAG, "sender_id==>$senderId")

        viewModelScope.launch {
            try {
                val repository = checkMobileNumberRepository.checkMobileRepository(hashMap)
                checkMobileNumberMutableLiveData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG, "SUCCESSSApi==>")

            } catch (e: Exception) {
                checkMobileNumberMutableLiveData.value = ShowStatus(Status.ERROR, null)
                Log.d(TAG, "ERRORAPi==>${e}")
            }
        }


    }
}