package ui.myBooking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityMyBookingBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import fragment.upcoming.UpcomingFragment
import fragment.past.PastFragment


@AndroidEntryPoint
class MyBookingActivity() : BaseActivity<ActivityMyBookingBinding, MyBookingViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_my_booking
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun setupObservable() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.myTripToolbar.txtTittle.text = getString(R.string.my_trips)
        binding.myTripToolbar.notification.visibility= View.GONE
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Past"))
        fragmentSet(UpcomingFragment())

        binding.myTripToolbar.imgHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        fragmentSet(UpcomingFragment())
                    }
                    1 -> {
                        fragmentSet(PastFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }

    fun fragmentSet(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout, fragment).commit()


    }

}