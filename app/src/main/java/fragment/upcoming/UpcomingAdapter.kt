package fragment.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ItemPastDataBinding
import model.UpcomingModel

class UpcomingAdapter(
    var context: Context,
    var upcomingModel: ArrayList<UpcomingModel.Data>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<UpcomingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPastDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = upcomingModel!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtCompited.setTextColor(context.getColor(R.color.sub_theme_color))

        holder.binding.txtPickUpAddress.text = upcomingModel[position].pickup_location
        holder.binding.txtDropOffAddress.text = upcomingModel[position].dropoff_location
        holder.binding.txtCompited.text = upcomingModel[position].status
        holder.binding.txtDistance.visibility= View.GONE
        holder.binding.txtKm.visibility= View.GONE
        holder.binding.txtTotalPrice.visibility= View.GONE
        holder.binding.txtPrice.visibility= View.GONE

        holder.binding.constraint.setOnClickListener {
            callback.invoke(position)
        }

    }

    inner class MyViewHolder(var binding: ItemPastDataBinding) :
        RecyclerView.ViewHolder(binding.root)
}
