package ui.tripHistory


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityTripHistory2Binding
import com.example.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dialog.DialogShow
import model.CancellationModel
import okhttp3.OkHttpClient
import okhttp3.Request
import ui.invoice.InvoiceActivity
import utils.MapData


@AndroidEntryPoint
class TripHistoryActivity() : BaseActivity<ActivityTripHistory2Binding, TripHistoryViewModel>(),
    OnMapReadyCallback {
    override val layoutId: Int
        get() = R.layout.activity_trip_history2
    override val bindingVariable: Int
        get() = BR.viewModel

    var startLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    var map: GoogleMap? = null
    private var destinationMarker = MarkerOptions()
    var startMarker = MarkerOptions()
    var zoomed = false
    var pickUpAddress: String? = null
    var dropUpAddress: String? = null
    lateinit var drawable: Drawable
    var upComingPosition: Int? = null
    var pastPosition: Int? = null
    var id: String? = ""
    var status: String? = ""
    lateinit var cancellationModel: ArrayList<CancellationModel>

    companion object {
        var TAG: String = TripHistoryActivity::class.java.simpleName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.tripHistoryToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tripHistoryToolbar.txtTittle.setText(R.string.tripDetail)
        binding.tripHistoryToolbar.notification.visibility = View.GONE

        mViewModel.tripApiCall()

        if (intent != null) {

            upComingPosition = intent.getIntExtra("UPCOMINGPOSITION", 0)
            pastPosition = intent.getIntExtra("PASTPOSITION", 0)
            Log.d(TAG, "upcomingPosition==>$upComingPosition")

            if (upComingPosition == 1) {
                pickUpAddress = intent.getStringExtra("pickAddress")
                dropUpAddress = intent.getStringExtra("dropAddress")
                status = intent.getStringExtra("status")
                id = intent.getStringExtra("id")


                val pickLat = intent.getStringExtra("pickLat")
                val pickLng = intent.getStringExtra("pickLag")
                val dropLat = intent.getStringExtra("dropLat")
                val dropLng = intent.getStringExtra("dropLng")


                Log.d(TAG, "data--->$pickUpAddress")
                Log.d(TAG, "data--->$dropUpAddress")
                Log.d(TAG, "data--->$pickLat")
                Log.d(TAG, "data--->$pickLng")
                Log.d(TAG, "data--->$dropLat")
                Log.d(TAG, "data--->$dropLng")
                Log.d(TAG, "data--->$status")
                Log.d(TAG, "data--->$id")

                startLocation = pickLat?.let { LatLng(it.toDouble(), pickLng!!.toDouble()) }

                if (dropLat != null) {
                    destinationLocation = LatLng(dropLat.toDouble(), dropLng!!.toDouble())
                }
                binding.pickUpAndDropUpData.txtDistance.visibility = View.GONE
                binding.pickUpAndDropUpData.txtKm.visibility = View.GONE
                binding.pickUpAndDropUpData.txtPrice.visibility = View.GONE
                binding.pickUpAndDropUpData.txtTotalPrice.visibility = View.GONE
                binding.pickUpAndDropUpData.txtCompited.text = status
                binding.pickUpAndDropUpData.txtCompited.setTextColor(getColor(R.color.sub_theme_color))
                binding.imgInvoice.visibility = View.GONE
                binding.txtInvoices.text = getString(R.string.CancelBooking)
                binding.txtInvoices.setTextColor(getColor(R.color.colorRed))
                binding.invoiceCardView.strokeColor = getColor(R.color.colorRed)
                binding.nameRatingImgConstraint.visibility = View.GONE

            } else if (pastPosition == 2) {
                pickUpAddress = intent.getStringExtra("pickUpAddress")
                dropUpAddress = intent.getStringExtra("dropOffAddress")
                status = intent.getStringExtra("status")

                val pickLat = intent.getStringExtra("pickupLat")
                val pickLng = intent.getStringExtra("pickupLag")
                val dropLat = intent.getStringExtra("dropLat")
                val dropLng = intent.getStringExtra("dropLng")

                Log.d(TAG, "data--->$pickUpAddress")
                Log.d(TAG, "data--->$dropUpAddress")
                Log.d(TAG, "data--->$pickLat")
                Log.d(TAG, "data--->$pickLng")
                Log.d(TAG, "data--->$dropLat")
                Log.d(TAG, "data--->$dropLng")

                startLocation = pickLat?.let { LatLng(it.toDouble(), pickLng!!.toDouble()) }
                if (dropLat != null) {
                    destinationLocation = LatLng(dropLat.toDouble(), dropLng!!.toDouble())
                }
                if (status == "cancelled") {
                    binding.pickUpAndDropUpData.txtCompited.setTextColor(getColor(R.color.colorRed))
                    binding.invoicelayout.visibility = View.GONE
                    binding.nameRatingImgConstraint.visibility = View.GONE
                } else if (status == "completed") {
                    binding.pickUpAndDropUpData.txtCompited.setTextColor(getColor(R.color.green))
                }

            }
            binding.pickUpAndDropUpData.txtPickUpAddress.text = pickUpAddress
            binding.pickUpAndDropUpData.txtDropOffAddress.text = dropUpAddress
            binding.pickUpAndDropUpData.txtCompited.text = status

            binding.invoicelayout.setOnClickListener {
                if (upComingPosition == 1) {
                    cancellationModel = arrayListOf()

                    cancellationModel.add(
                        CancellationModel(
                            false,
                            getString(R.string.driver_delay)
                        )
                    )
                    cancellationModel.add(
                        CancellationModel(
                            false,
                            getString(R.string.changedThePlan)
                        )
                    )
                    cancellationModel.add(
                        CancellationModel(
                            false,
                            getString(R.string.longPickUpTime)
                        )
                    )
                    cancellationModel.add(
                        CancellationModel(
                            false,
                            getString(R.string.noLongIntersted))
                    )
                    Log.d(TAG, "id==>$id")

                    cancellationModel.add(CancellationModel(false, getString(R.string.other)))
                    if (cancellationModel.isEmpty()) {
                        Toast.makeText(this, "please Select Option", Toast.LENGTH_SHORT).show()
                    }
                   DialogShow(this).bottomSheet(
                        txtAddress = "",
                        true,
                        this,
                        { mViewModel.cancelTripApiCall(id = id!!) },{},
                        "cancelTripApi", null,cancellationModel.toString()
                    )


                } else if (pastPosition == 2) {
                    var intent = Intent(this, InvoiceActivity::class.java)
                    intent.putExtra("pickupAdress", pickUpAddress)
                    intent.putExtra("dropOffAdress", dropUpAddress)
                    startActivity(intent)
                }
            }
        }
    }


    @SuppressLint("ResourceType")
    override fun setupObservable() {
        mViewModel.setNavigator(this)
        mViewModel.tripLiveData().observe(this) {
            when (it.status) {

                Status.SUCCESS -> {
                    Log.d(TAG, "id==>${it.data?.data}")
                    val pickAddress = it.data?.data?.pickup_location
                    val dropOffPickUp = it.data?.data?.dropoff_location



                    if (!zoomed) {
                        map?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                startLocation!!, 10.5F
                            )
                        )
                        map?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                destinationLocation!!,
                                10.5F
                            )
                        )
                        zoomed = true
                    }
                    Log.d(TAG, "PICKLAT====>$startLocation")
                    Log.d(TAG, "DROPLat====>$destinationLocation")

                    val builder = LatLngBounds.Builder()
                    builder.include(startLocation!!)
                    builder.include(destinationLocation!!)

                    startMarker.position(startLocation!!).icon(
                        resizeBitmap(
                            R.drawable.live_marker_img.toString(),
                            72,
                            150
                        )?.let { it1 ->
                            BitmapDescriptorFactory.fromBitmap(
                                it1
                            )
                        })

                    map?.addMarker(startMarker.position(startLocation!!))
                    destinationMarker.position(destinationLocation!!).icon(
                        resizeBitmap(
                            R.drawable.img_marker_car.toString(),
                            72,
                            72
                        )?.let { it1 ->
                            BitmapDescriptorFactory.fromBitmap(
                                it1
                            )
                        })
                    map?.addMarker(destinationMarker.position(destinationLocation!!))

                    val url = startLocation?.let {
                        destinationLocation?.let { it1 ->
                            getDirectionURL(
                                it,
                                it1,
                                "AIzaSyAHoxA9mAOwiilUUwLfauECc7SrJSNwywM"
                            )
                        }
                    }
                    Log.e("LOCATION", "curruntLatLng: $startLocation")
                    if (url != null) {
                        GetDirection(url).execute()
                    }
                }

                Status.ERROR -> {
                    Log.d(TAG, "ERROR===>")
                }

                Status.LOADING -> {
                    Log.d(TAG, "LOADING===>")
                }
            }
        }

        mViewModel.cancelTripLiveData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    onBackPressedDispatcher.onBackPressed()
//                    startActivity(Intent(this, MyBookingActivity::class.java))
//                    finish()
                    Log.d(TAG, "SUCCESS===>${it.data}")
                }

                Status.ERROR -> {
                    Log.d(TAG, "ERROR===>")
                }

                Status.LOADING -> {
                    Log.d(TAG, "LOADING===>")


                }

            }

        }
    }


    override fun onMapReady(p0: GoogleMap) {
        map = p0
        var marker = MarkerOptions()
        map!!.setOnMapClickListener { latlng ->
            marker.position(latlng)
            map?.addMarker(marker)
        }
        map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
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

    fun resizeBitmap(drawableName: String?, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                drawableName, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }


}