package fragment.past

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import base.BaseViewModel
import com.example.utils.Status
import model.PastModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import utils.RequestCodeCheck
import utils.ShowStatus
import java.lang.Exception
import javax.inject.Inject

class PastViewModel @Inject constructor(var context: Context) : BaseViewModel<Any>() {

    var TAG=PastViewModel::class.java.simpleName
    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var requestCodeCheck: RequestCodeCheck

    @Inject
    lateinit var pastRepository: PastRepository

    private val pastMutableData: MutableLiveData<ShowStatus<PastModel>> = MutableLiveData()
    val pastLiveData: LiveData<ShowStatus<PastModel>> = pastMutableData


    fun pastAPiCall() {
        viewModelScope.launch {
            try {
                val repository = pastRepository.pastRepositoryData()
                pastMutableData.postValue(requestCodeCheck.getResponse(repository))
                Log.d(TAG,"SUCCESS")


            }catch (e:Exception){
                pastMutableData.value= ShowStatus(Status.ERROR,null,)
                Log.d(TAG,"ERROR--->$e")
            }


        }
    }

}