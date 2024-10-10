package fragment.upcoming

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import base.BaseFragment
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.FragmentUpcomingBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import model.UpcomingModel
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import ui.tripHistory.TripHistoryActivity

@AndroidEntryPoint
class UpcomingFragment() : BaseFragment<FragmentUpcomingBinding, UpcomingViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_upcoming
    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG = UpcomingFragment::class.java.simpleName
    }

    var upcomingAdapter: UpcomingAdapter? = null
    lateinit var upcomingModel: ArrayList<UpcomingModel.Data>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        upcomingModel = arrayListOf()
        viewModel.upcomingApiCall()
        upcomingAdapter?.notifyItemChanged(upcomingModel.size)

        return super.onCreateView(
            inflater, container, savedInstanceState
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObservable() {
        viewModel.setNavigator(this)
        binding.imgLoader.loader.visibility = View.VISIBLE
        binding.imgLoader.loader.show()
        viewModel.upcomingLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    val data = it.data?.data
                    if (data != null) {
                        upcomingModel = data
                    }
                    Log.d(TAG, "UPCOMINGDATA--->${it.data?.data}")
                    upcomingAdapter =
                        UpcomingAdapter(requireActivity(), it.data!!.data) { position ->

                            val pickupaddress = it.data.data[position].pickup_location
                            val dropOffAddress = it.data.data[position].dropoff_location
                            val pickupLat = it.data.data[position].pickup_lat
                            val pickupLag = it.data.data[position].pickup_lng
                            val dropLat = it.data.data[position].dropoff_lat
                            val dropLng = it.data.data[position].dropoff_lng
                            val status = it.data.data[position].status
                            val id = it.data.data[position].id

                            Toast.makeText(requireContext(), "selected", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireActivity(), TripHistoryActivity::class.java)
                            intent.putExtra("pickAddress", pickupaddress)
                            intent.putExtra("dropAddress", dropOffAddress)
                            intent.putExtra("pickLat", pickupLat)
                            intent.putExtra("pickLag", pickupLag)
                            intent.putExtra("dropLat", dropLat)
                            intent.putExtra("dropLng", dropLng)
                            intent.putExtra("status", status)
                            intent.putExtra("id", id)
                            intent.putExtra("UPCOMINGPOSITION", 1)
                            requireContext().startActivity(intent)
                            upcomingAdapter!!.notifyItemChanged(position)
                        }
                    binding.upcomingRecycler.adapter = upcomingAdapter


                    if (data!!.isEmpty()) {
                        binding.dataNotFound.visibility = View.VISIBLE
                    }
                }

                Status.ERROR -> {
                    Log.d(TAG, "Error")
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                }

                Status.LOADING -> {
                    binding.imgLoader.loader.visibility = View.VISIBLE
                    binding.imgLoader.loader.show()
                }

            }
        }
    }
}