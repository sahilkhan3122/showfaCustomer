package fragment.past

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import base.BaseFragment
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.FragmentPastBinding
import com.example.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import model.PastModel
import ui.tripHistory.TripHistoryActivity

@AndroidEntryPoint
class PastFragment() : BaseFragment<FragmentPastBinding, PastViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_past

    override val bindingVariable: Int
        get() = BR.viewModel

    companion object {
        var TAG = PastFragment::class.java.simpleName
    }

    var pastAdapter: PastAdapter? = null
    lateinit var pastModel: ArrayList<PastModel.Data>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pastModel = arrayListOf()
        viewModel.pastAPiCall()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObservable() {
        binding.imgLoader.loader.visibility = View.VISIBLE
        binding.imgLoader.loader.show()
        viewModel.setNavigator(this)
        viewModel.pastLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.imgLoader.loader.visibility = View.INVISIBLE
                    binding.imgLoader.loader.hide()
                    if (it.data!!.status) {

                        Log.d(TAG, "PASTDATA--->${it.data.data}")
                        var data = it.data.data
                        pastAdapter = PastAdapter(requireContext(), data) { position ->
                            val pickupaddress = it.data.data[position].pickup_location
                            val dropOffAddress = it.data.data[position].dropoff_location
                            val pickupLat = it.data.data[position].pickup_lat
                            val pickupLag = it.data.data[position].pickup_lng
                            val dropLat = it.data.data[position].dropoff_lat
                            val dropLng = it.data.data[position].dropoff_lng
                            val status = it.data.data[position].status

                            val intent = Intent(requireContext(), TripHistoryActivity::class.java)
                            intent.putExtra("pickUpAddress", pickupaddress)
                            intent.putExtra("dropOffAddress", dropOffAddress)
                            intent.putExtra("pickupLat", pickupLat)
                            intent.putExtra("pickupLag", pickupLag)
                            intent.putExtra("dropLat", dropLat)
                            intent.putExtra("dropLng", dropLng)
                            intent.putExtra("status", status)
                            intent.putExtra("PASTPOSITION", 2)
                            requireContext().startActivity(intent)
                        }
                        binding.pastRecycler.adapter = pastAdapter
                        pastAdapter!!.notifyDataSetChanged()

                        if (data.isEmpty()) {
                            binding.dataNotFound.visibility = View.VISIBLE
                        }
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