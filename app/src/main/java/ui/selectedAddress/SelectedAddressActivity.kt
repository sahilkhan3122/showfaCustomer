package ui.selectedAddress

import network.GoogleSearchInterface
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivitySelectedAddressBinding
import com.example.utils.Status
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import model.ListClass
import model.MainPojo
import model.PlaceIdLatLngModel
import model.SaveRecentPlaceModel
import model.SaveRecentPlayListModel
import kotlinx.coroutines.Runnable
import network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.homeActivity.HomeActivity
import ui.savedPlaces.SavedPlacesActivity
import utils.ShowStatus


@AndroidEntryPoint
class SelectedAddressActivity() :
    BaseActivity<ActivitySelectedAddressBinding, SelectedViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_selected_address
    override val bindingVariable: Int
        get() = BR.viewModel

    private var searchPlaceText = ""
    lateinit var handler: Handler
    var runnable: Runnable? = null
    var apiInterface: ApiInterface? = null
    var googleSearchInterface: GoogleSearchInterface? = null
    private var selectedRecentAdapter: SelectedRecentAdapter? = null
    var selectedAddressListAdapter: SelectAddressListAdapter? = null
    lateinit var saveRecentPlayListList: ArrayList<SaveRecentPlayListModel.Data>
    lateinit var saveRecentPlaceModel: ArrayList<SaveRecentPlaceModel>
    lateinit var dataList: ArrayList<ListClass>
    lateinit var placeIDLatLngModel: ArrayList<PlaceIdLatLngModel>
    private var count: Int? = null
    var DroupoffLat: Double? = null
    var DroupoffLng: Double? = null

    companion object {
        var TAG: String = SelectedAddressActivity::class.java.simpleName
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataList = arrayListOf()
        saveRecentPlayListList = arrayListOf()
        placeIDLatLngModel = arrayListOf()

        binding.toolbar.txtTittle.text = resources.getString(R.string.search_place)
        binding.edtSearchPlace.requestFocus()
        setupSearchGoogleUrlAndInterface()
        editOnChatListener()
//        mViewModel.savedAddressApiCall()
        binding.toolbar.imgHeader.setImageResource(R.drawable.ic_back_button)

        binding.toolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        handler = Handler()
        binding.txtSavedPlaces.setOnClickListener {
            startActivity(Intent(this, SavedPlacesActivity::class.java))

        }

        if (intent != null) {
            count = intent.extras?.getInt("count")
            Log.d(TAG, "count---->$count")
        }
        runnable = java.lang.Runnable { searchGoogleAddressListApiCall() }
        selectedRecentAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObservable() {

        mViewModel.setNavigator(this)

        mViewModel.getSavedRecentPlaceLiveData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.status) {
                        val linearLayoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        Log.d(TAG, "AlladdressList-->${it.data.data}")

                        selectedRecentAdapter =
                            SelectedRecentAdapter(this, it.data.data) { position ->
                            }
                        binding.recyclerAddressList.layoutManager = linearLayoutManager
                        binding.recyclerAddressList.adapter = selectedRecentAdapter
                        selectedRecentAdapter!!.notifyDataSetChanged()
                    }
                }

                Status.ERROR -> {
                    ShowStatus.error(null, "Wrong")
                }

                Status.LOADING -> {
                    Log.d(TAG, "Loading----->")
                }
            }
        }

    }

    private fun editOnChatListener() {
        binding.edtSearchPlace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                searchPlaceText = s.toString()
                runnable?.let { handler.removeCallbacks(it) }
                if (s!!.length >= 3) {
                    Toast.makeText(
                        this@SelectedAddressActivity,
                        "selected city",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.recyclerAddressList.visibility = View.VISIBLE
                    binding.recyclerAddressListRecent.visibility = View.GONE
                    binding.txtSavedPlaces.visibility = View.GONE

                    runnable?.let { handler.postDelayed(it, 500) }
                } else {
                    binding.recyclerAddressList.visibility = View.GONE
                    binding.recyclerAddressListRecent.visibility = View.VISIBLE
                }
                getListShow()
            }

            private fun getListShow() {

                val linearLayoutManager =
                    LinearLayoutManager(
                        this@SelectedAddressActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                Log.d(TAG, "dataList--->$dataList")
                selectedAddressListAdapter =
                    SelectAddressListAdapter(this@SelectedAddressActivity, dataList) { position ->
                        if (count == 1) {
                            latLngApiCall(dataList[position].placeId, position)
                        } else {
                            latLngApiCall(dataList[position].placeId, position)
                        }
                        selectedAddressListAdapter?.notifyItemChanged(position)
                    }
                binding.recyclerAddressList.layoutManager = linearLayoutManager
                binding.recyclerAddressList.adapter = selectedAddressListAdapter
            }
        })

    }

    private fun setupSearchGoogleUrlAndInterface() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .build()
        googleSearchInterface = retrofit.create(GoogleSearchInterface::class.java)
    }


    private fun searchGoogleAddressListApiCall() {
        val country = "country:in|country:ke"
        Log.e(TAG, "getData: $searchPlaceText")
        if (searchPlaceText.length >= 3) {

            googleSearchInterface?.getSearch(
                searchPlaceText, country, getString(R.string.map_api_key)
            )
                ?.enqueue(object : Callback<MainPojo?> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<MainPojo?>,
                        response: Response<MainPojo?>
                    ) {
                        if (response.isSuccessful) {

                            Log.d(TAG, "response--->${response.body()}")
                            if (response.body() != null) {
                                if (response.body()!!.predictions != null) {

                                    var gson = Gson()
                                    var data = gson.toJson(response.body())
                                    Log.d("data", "predictions--->${data}")
                                    dataList.clear()
                                    dataList.addAll(response.body()?.predictions!!)
                                    Log.d("data", "dataList--->$dataList")
                                    binding.recyclerAddressList.adapter?.notifyDataSetChanged()
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<MainPojo?>, t: Throwable) {
                        Log.d("data", "Error--->")

                    }
                })
        }
    }

    fun latLngApiCall(placeid: String, position: Int) {
        googleSearchInterface?.getLatlng("place/details/json?placeid=${placeid}&key=AIzaSyAXb1W-pTI0-1uedHOuR_0uLVqqfRCCYXk")
            ?.enqueue(object : Callback<PlaceIdLatLngModel?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<PlaceIdLatLngModel?>,
                    response: Response<PlaceIdLatLngModel?>
                ) {
                    if (response.isSuccessful) {

                        Log.d(TAG, "1234--->${response.body()}")
                        if (response.body() != null) {

                            var gson = Gson()
                            var data = gson.toJson(response.body())
                            Log.d("data", "predictions--->${data}")
                            DroupoffLat = response.body()!!.result.geometry.location.lat
                            DroupoffLng = response.body()!!.result.geometry.location.lng

                            Log.d("data", "lat--->$DroupoffLat")
                            Log.d("data", "lng--->$DroupoffLng")

                            if (count == 1) {
                                val intent =
                                    Intent(this@SelectedAddressActivity, HomeActivity::class.java)
                                intent.putExtra("DropOff", dataList[position].description)
                                intent.putExtra("Latitude", DroupoffLat)
                                intent.putExtra("longitude", DroupoffLng)
                                Log.d(TAG, "selectedLat---->$DroupoffLat")
                                Log.d(TAG, "selectedLng---->$DroupoffLng")
                                startActivity(intent)

                            } else {
                                val intent =
                                    Intent(this@SelectedAddressActivity, HomeActivity::class.java)
                                intent.putExtra("PickUp", dataList[position].description)
                                intent.putExtra("PickUpLat", DroupoffLat)
                                intent.putExtra("PickUpLng", DroupoffLng)
                                Log.d(TAG, "pickuplat---->$DroupoffLat")
                                Log.d(TAG, "pickuplat---->$DroupoffLng")
                                startActivity(intent)
                            }

                        }

                    }
                }

                override fun onFailure(call: Call<PlaceIdLatLngModel?>, t: Throwable) {
                    Log.d("data", "Error--->")

                }
            })
    }

}
