package ui.homeActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import base.BaseActivity
import com.bumptech.glide.Glide
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityHomeBinding
import com.example.utils.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import model.DrawerModel
import dialog.DialogShow
import extension.showSnackBar
import laungage.MyDataStore
import okhttp3.OkHttpClient
import okhttp3.Request
import ui.home.DrawerAdapter
import ui.home.HomeViewModel
import ui.legal.LegalActivity
import ui.myBooking.MyBookingActivity
import ui.notification.NotificationActivity
import ui.previousDue.PreviousDueActivity
import ui.promocodes.PromocodesActivity
import ui.register.RegisterActivity
import ui.savedPlaces.SavedPlacesActivity
import ui.selectedAddress.SelectedAddressActivity
import ui.support.SupportActivity
import ui.wallet.WalletActivity
import utils.API_CONSTANTS.URL
import utils.MapData
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity() : BaseActivity<ActivityHomeBinding, HomeViewModel>(), OnMapReadyCallback {
    override val layoutId: Int
        get() = R.layout.activity_home
    override val bindingVariable: Int
        get() = BR.viewModel

    @Inject
    lateinit var myDataStore: MyDataStore
    var position: Int? = null
    lateinit var bottomSheetDialog: BottomSheetDialog

    companion object {
        var TAG: String = HomeActivity::class.java.simpleName
    }

    lateinit var drawableModel: ArrayList<DrawerModel>
    var map: GoogleMap? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var id: String

    var currentLocationLatLng: LatLng? = null
    var destinationLocationLatLng: LatLng? = null
    private var zoomed = false
    var dropOffLat: Double? = 0.0
    var dropOffLng: Double? = 0.0
    var addMarker = MarkerOptions()

    var pickUpLat: Double? = 0.0
    var pickUpLng: Double? = 0.0
    var pickUpData: String? = ""
    var dropOffData: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.toolbarLayout.imgHeader.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setDrawerRecycler()
        getIntentData()
        drawerLayoutClickEvent()

        binding.imgLocation.setOnClickListener {
            map!!.clear()
            getCurrentLocation()
        }

        binding.toolbarLayout.notification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        myDataStore.getUSerFirstName.asLiveData().observe(this) {
            binding.drawableItems.txtFirstName.text = it
            binding.toolbarLayout.txtFirstName.visibility = View.VISIBLE
            binding.toolbarLayout.txtFirstName.text = it
        }

        myDataStore.getUSerlastName.asLiveData().observe(this) {
            binding.drawableItems.txtLastName.text = it
            binding.toolbarLayout.txtLastName.visibility = View.VISIBLE
            binding.toolbarLayout.txtLastName.text = it

        }

        myDataStore.getProfile.asLiveData().observe(this) {
            Log.d(TAG, "profileImage====>$it")
            Glide.with(this).load(URL+it).into(binding.drawableItems.headerImage)
            Log.d(TAG, "profileImage====>${URL + it}")
        }

        binding.pickUpDropOffCard.txtPickUpAddress.setOnClickListener {
            val intent = Intent(this, SelectedAddressActivity::class.java)
            intent.putExtra("count", 0)
            startActivity(intent)
        }

        binding.pickUpDropOffCard.txtDropOffAddress.setOnClickListener {
            val intent = Intent(this, SelectedAddressActivity::class.java)
            intent.putExtra("count", 1)
            startActivity(intent)
        }

        binding.pickUpDropOffCard.imgDroupOffFavourite.setOnClickListener {
            if (binding.pickUpDropOffCard.txtDropOffAddress.text.isEmpty()) {
                binding.root.showSnackBar(getString(R.string.first_add_dropoff_location))
            } else {

                bottomSheetDialog=DialogShow(this).bottomSheet(
                    binding.pickUpDropOffCard.txtDropOffAddress.text.toString(),
                    ischecked = true,
                    this,
                    btnAddPlaceClick = {
                        mViewModel.addFavApiCall(
                            binding.pickUpDropOffCard.txtPickUpAddress.text.toString(),
                            destinationLocationLatLng
                        )
                    }, {}, "imgDroupOffFavourite"
                )
            }

        }
    }

    private fun drawerLayoutClickEvent() {

        binding.drawableItems.constraint.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("index", 1)
            startActivity(intent)
        }

        binding.drawableItems.DeleteMyAccount.setOnClickListener {
            myDataStore.getUSerId.asLiveData().observe(this) {
                Log.d(TAG, "DELETEACCOUNTID==>$it")
                binding.drawableItems.imgLoader.loader.visibility = View.VISIBLE
                binding.drawableItems.imgLoader.loader.show()
                mViewModel.deleteAccountApiCall(it)
            }

        }

        binding.drawableItems.materialCardView.setOnClickListener {
            binding.drawableItems.imgLoader.loader.visibility = View.VISIBLE
            binding.drawableItems.imgLoader.loader.show()
            DialogShow(this@HomeActivity).sessionExpiredDialogShow(
                this@HomeActivity,
                getString(R.string.are_you_sure_logout),
                getString(R.string.logout),
                getString(R.string.cancel),
                2, myDataStore
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getIntentData() {
        if (intent != null) {
            pickUpData = intent.extras?.getString("PickUp")
            pickUpLat = intent.extras?.getDouble("PickUpLat")
            pickUpLng = intent.extras?.getDouble("PickUpLng")

            dropOffData = intent.extras?.getString("DropOff")
            dropOffLat = intent.extras?.getDouble("Latitude")
            dropOffLng = intent.extras?.getDouble("longitude")

            //addPlaces add to droupoff
            Log.d(TAG, "position===>$position")

            position = intent.extras?.getInt("position")

            if (position == 1) {
                dropOffData = intent.extras?.getString("dropOffAddress")
                dropOffLat = intent.extras?.getDouble("dropOffLat")
                dropOffLng = intent.extras?.getDouble("dropOffLng")

                Log.d(TAG, "dropoff111---->${dropOffData}")
                Log.d(TAG, "dropoffLat---->${dropOffLat}")
                Log.d(TAG, "dropoffLng---->${dropOffLng}")

               destinationLocationLatLng = dropOffLat?.let { LatLng(it, dropOffLng!!) }

            }

            destinationLocationLatLng = dropOffLat?.let { LatLng(it, dropOffLng!!) }

            Log.d(TAG, "pickUp---->${pickUpData}")
            Log.d(TAG, "dropoff---->${dropOffData}")
            Log.d(TAG, "dropoffLat---->${dropOffLat}")
            Log.d(TAG, "dropoffLng---->${dropOffLng}")

            Log.d(TAG, "pickUpLat---->${pickUpLat}")
            Log.d(TAG, "pickUpLng---->${pickUpLng}")
            Log.d(TAG, "destinationLocationLatLng---->${destinationLocationLatLng}")


            if (pickUpData != null) {
                binding.pickUpDropOffCard.txtPickUpAddress.text = pickUpData
            } else if (dropOffData != null) {
                binding.pickUpDropOffCard.txtDropOffAddress.text = dropOffData
            }
            binding.pickUpDropOffCard.imgPickUpFavourite.setOnClickListener {
                bottomSheetDialog=DialogShow(this).bottomSheet(
                    binding.pickUpDropOffCard.txtPickUpAddress.text.toString(),
                    ischecked = true,
                    this,
                    btnAddPlaceClick = {
                        binding.drawableItems.imgLoader.loader.visibility = View.VISIBLE
                        binding.drawableItems.imgLoader.loader.show()
                        mViewModel.addFavApiCall(
                            binding.pickUpDropOffCard.txtPickUpAddress.text.toString(),
                            currentLocationLatLng
                        )
                    }, {}, "imgPickUpFavourite"
                )
            }
            if (binding.pickUpDropOffCard.txtPickUpAddress.text.isNotEmpty() && binding.pickUpDropOffCard.txtDropOffAddress.text.isNotEmpty()) {
//                binding.chooseRideOrSwipeUpMore.visibility = View.VISIBLE
//                binding.constraintRecyclerLayout.visibility = View.VISIBLE
                binding.bottomConstraint.visibility = View.VISIBLE
            }
        }
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.addFavLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.drawableItems.imgLoader.loader.visibility = View.INVISIBLE
                    binding.drawableItems.imgLoader.loader.hide()

                    DialogShow(this).networkDialog(
                        txtTitle = View.GONE, buttonClose = View.GONE, txtmessage = it.data?.message
                            ?: "", txtConnection = View.GONE, 3, { bottomSheetDialog.dismiss() }
                    )
                }

                Status.ERROR -> {
                    Log.d(TAG, "ERROR")
                    binding.drawableItems.imgLoader.loader.visibility = View.INVISIBLE
                    binding.drawableItems.imgLoader.loader.hide()
                }

                Status.LOADING -> {
                    binding.drawableItems.imgLoader.loader.visibility = View.VISIBLE
                    binding.drawableItems.imgLoader.loader.show()
                }
            }
        }
        mViewModel.deleteAccountLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.drawableItems.imgLoader.loader.visibility = View.INVISIBLE
                    binding.drawableItems.imgLoader.loader.hide()
                }

                Status.ERROR -> {
                    Log.d(TAG, "ERROR")
                    binding.drawableItems.imgLoader.loader.visibility = View.INVISIBLE
                    binding.drawableItems.imgLoader.loader.hide()
                }

                Status.LOADING -> {
                    binding.drawableItems.imgLoader.loader.visibility = View.VISIBLE
                    binding.drawableItems.imgLoader.loader.show()

                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setDrawerRecycler() {
        drawableModel = arrayListOf()

        drawableModel.add(DrawerModel(R.drawable.ic_trip_history, getString(R.string.trip_history)))
        drawableModel.add(DrawerModel(R.drawable.ic_previous_due, getString(R.string.previous_due)))
        drawableModel.add(DrawerModel(R.drawable.ic_menu_wallet, getString(R.string.wallet)))
        drawableModel.add(DrawerModel(R.drawable.ic_promo_icon, getString(R.string.promo_codes)))
        drawableModel.add(DrawerModel(R.drawable.ic_trip_history, getString(R.string.saved_places)))
        drawableModel.add(DrawerModel(R.drawable.ic_support, getString(R.string.support)))
        drawableModel.add(DrawerModel(R.drawable.ic_legal, getString(R.string.legal)))

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val drawerAdapter = DrawerAdapter(this, drawableModel) { position ->
            var intent = Intent()
            if (position == 0) {
                intent = Intent(this, MyBookingActivity::class.java)
            } else if (position == 1) {
                intent = Intent(this, PreviousDueActivity::class.java)
            } else if (position == 2) {
                intent = Intent(this, WalletActivity::class.java)
            } else if (position == 3) {
                intent = Intent(this, PromocodesActivity::class.java)
            } else if (position == 4) {
                intent = Intent(this, SavedPlacesActivity::class.java)
            } else if (position == 5) {
                intent = Intent(this, SupportActivity::class.java)
            } else if (position == 6) {
                intent = Intent(this, LegalActivity::class.java)
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(intent)

        }

        binding.drawableItems.drawerRecyclerView.layoutManager = layoutManager
        binding.drawableItems.drawerRecyclerView.adapter = drawerAdapter
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        val marker1 = MarkerOptions()
        getCurrentLocation()

        if (dropOffLat != null && dropOffLng != null) {
            val droupOffLatLng = LatLng(dropOffLat!!, dropOffLng!!)
            val pickUp = LatLng(23.0710581, 72.5177969)
            droupOffLatLng?.let { marker1.position(it) }

            Log.d(TAG, "sydney---->$droupOffLatLng")
            Log.d(TAG, "sydney---->$currentLocationLatLng")

            if (currentLocationLatLng != null) {

                map!!.clear()
                map!!.moveCamera(CameraUpdateFactory.newLatLng(droupOffLatLng))
                map!!.addMarker(marker1.position(droupOffLatLng).title("Marker in Sydney"))
            } else if (currentLocationLatLng != null) {

                map!!.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLng!!))
                map!!.addMarker(addMarker.position(currentLocationLatLng!!))

            } else {
                droupOffLatLng?.let { marker1.position(it).title("Marker in Sydney") }
                    ?.let { map!!.addMarker(it) }
                val url = getDirectionURL(
                    pickUp,
                    droupOffLatLng,
                    "AIzaSyAHoxA9mAOwiilUUwLfauECc7SrJSNwywM"
                )
                Log.e("LOCATION", "curruntLatLng: $pickUp")
                GetDirection(url).execute()

                Log.e("LOCATION", "currunt: $pickUp")
            }

            googleMap.uiSettings.isZoomControlsEnabled = true

        } else {
            Toast.makeText(this, "dropOfLatLng null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
            val location = task.result
            if (location != null) {
                currentLocationLatLng = LatLng(location.latitude, location.longitude)
                Log.d(TAG, "CURRUNTLOCATION---->${location.latitude}")

                currentLocationMaker(currentLocationLatLng!!)
                if (!zoomed) {
                    map?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLocationLatLng!!,
                            17.7f
                        )
                    )
                    zoomed = true
                }


            }
        }
    }

    fun currentLocationMaker(locationLatLong: LatLng) {
        addMarker =
            MarkerOptions().position(locationLatLong)
                .title("" + locationLatLong)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.live_marker_img))
        map?.addMarker(addMarker)
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {

            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(getColor(R.color.theme_color))
            }
            map!!.addPolyline(lineoption)
        }
    }


}

