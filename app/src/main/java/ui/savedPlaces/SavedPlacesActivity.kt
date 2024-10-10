package ui.savedPlaces

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivitySavedPlacesBinding
import com.example.utils.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import laungage.MyDataStore
import model.FavAddressListModel
import ui.homeActivity.HomeActivity
import utils.API_CONSTANTS.mutableLiveData
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class SavedPlacesActivity() : BaseActivity<ActivitySavedPlacesBinding, SavedPlacesViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_saved_places
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG: String = SavedPlacesActivity::class.java.simpleName
    }

    @Inject
    lateinit var myDataStore: MyDataStore
    var id: String? = ""
    var pickaddress: String? = ""
    var pickLat: String? = ""
    var pickLng: String? = ""
    var getAddress: String = ""
    var getLat: String = ""
    var getLng: String = ""
    var favtype: String? = ""
    var curuntPosition: Int=0
    lateinit var startAutocomplete: ActivityResultLauncher<Intent>
    var savedPlacesAdapter: SavedPlacesAdapter? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var addFavList: ArrayList<FavAddressListModel.Data>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.savedPlacesToolbar.txtTittle.text = getString(R.string.saved_places)
        binding.savedPlacesToolbar.notification.visibility = View.GONE

        myDataStore.getUSerId.asLiveData().observe(this) {
            id = it
            Log.d(TAG, "ID===>$id")
            mViewModel.getFavListApiCall(id!!)
        }
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.map_api_key), Locale.US);
        }

        startAutocomplete =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    if (intent != null) {
                        val place = Autocomplete.getPlaceFromIntent(intent)
                        Log.d(
                            TAG,
                            "Place: ${place.name}, ${place.address},${place.id},${place.latLng.latitude},${place.latLng.longitude}"
                        )
                        getAddress = place.address as String
                        getLat = place.latLng?.latitude.toString()
                        getLng = place.latLng?.longitude.toString()
                        getAddress = place.address as String
                        mutableLiveData.postValue(getAddress)
                        savedPlacesAdapter?.notifyDataSetChanged()

                    }
                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.d(TAG, "User canceled autocomplete")
                }
            }


        binding.savedPlacesToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.txtAddNewPlaces.setOnClickListener {
            Log.d(TAG,"favType==>$favtype")
            bottomSheetDialog = DialogShow(this).bottomSheet(
                txtAddress = getAddress,
                true,
                this,
                btnAddPlaceClick = { favtype?.let { it1 ->
                    mViewModel.addFavApicall(id!!, getAddress, getLat, getLng,
                        it1
                    )
                } },
                {},
                "AddNewPlaces",
                null,
                "",
                { intentBuilderAutoComplete() })

        }
        savedPlacesAdapter?.notifyDataSetChanged()


    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.favAddressListAddressLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d(TAG, "Success==>${it.data?.data}")
                    addFavList= it.data!!.data
                    val layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    savedPlacesAdapter =
                        SavedPlacesAdapter(this, it.data!!.data) { position ->
                            val homeActivityIntent=Intent(this,HomeActivity::class.java)
                            homeActivityIntent.putExtra("dropOffAddress",it.data.data[position].pickupLocation)
                            homeActivityIntent.putExtra("dropOffLat",it.data.data[position].pickupLat)
                            homeActivityIntent.putExtra("dropOffLng",it.data.data[position].pickupLng)
                            homeActivityIntent.putExtra("position",1)
                            startActivity(homeActivityIntent)
                            curuntPosition = position
                        }
                    favtype = addFavList[curuntPosition].favouriteType
                    Log.d(TAG,"setFavtype==>$favtype")
                    Log.d(TAG,"curuntPosition==>$curuntPosition")
                    binding.savedPlacesRecycler.layoutManager = layoutManager
                    binding.savedPlacesRecycler.adapter = savedPlacesAdapter

                    curuntPosition?.let { it1 -> savedPlacesAdapter?.notifyItemChanged(it1) }

                }

                Status.ERROR -> {
                    Log.d(TAG, "ERROR")
                }

                Status.LOADING -> {
                    Log.d(HomeActivity.TAG, "LOADING")

                }
            }
        }
        mViewModel.addFavAddressLiveData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d(TAG, "DATA==>${it.data?.data}")
                    DialogShow(this).networkDialog(
                        txtTitle = View.GONE,
                        buttonClose = View.GONE,
                        txtmessage = it.data!!.message,
                        txtConnection = View.GONE,
                        cancle = { bottomSheetDialog.dismiss() },
                    )
                    savedPlacesAdapter?.notifyDataSetChanged()

                }
                Status.ERROR -> {
                    Log.d(TAG, "ERROR")
                }

                Status.LOADING -> {
                    Log.d(HomeActivity.TAG, "LOADING")

                }
            }
        }


    }

    fun intentBuilderAutoComplete() {
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startAutocomplete.launch(intent)

    }
}