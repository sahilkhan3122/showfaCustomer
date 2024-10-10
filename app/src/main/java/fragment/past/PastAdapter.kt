package fragment.past

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ItemPastDataBinding
import model.PastModel
import utils.DateAndDayFormat.getFormatDateForDay

class PastAdapter(
    var context: Context,
    var pastModel: ArrayList<PastModel.Data>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<PastAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPastDataBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = pastModel!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.txtPickUpAddress.text = pastModel[position].pickup_location
        holder.binding.txtDropOffAddress.text = pastModel[position].dropoff_location
        holder.binding.txtCompited.text = pastModel[position].status

        if (pastModel[position].distance.isEmpty()) {
            holder.binding.txtKm.text = context.getString(R.string.km)
        } else {
            holder.binding.txtKm.text = pastModel[position].distance
        }

        if (holder.binding.txtCompited.text == "completed") {
            holder.binding.txtCompited.setTextColor(context.getColor(R.color.green))
        } else if (holder.binding.txtCompited.text == "cancelled") {
            holder.binding.txtCompited.setTextColor(context.getColor(R.color.colorRed))
        } else if (pastModel[position].pickup_date_time != null) {
            holder.binding.txtDayDateAndTime.text =
                getFormatDateForDay(pastModel[position].pickup_date_time)
        }

        holder.binding.constraint.setOnClickListener {
            callback.invoke(position)
        }
    }

    inner class MyViewHolder(var binding: ItemPastDataBinding) :
        RecyclerView.ViewHolder(binding.root)
}
