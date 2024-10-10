package ui.previousDue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemPreviousDueBinding

class PreviousDueAdapter(
    var context: Context, var previousDueModel: ArrayList<Any>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<PreviousDueAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPreviousDueBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.includePickUpDropOff.imgPickUpFavourite.visibility = View.INVISIBLE
        holder.binding.includePickUpDropOff.imgDroupOffFavourite.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int = previousDueModel!!.size

    inner class MyViewHolder(var binding: ItemPreviousDueBinding) :
        RecyclerView.ViewHolder(binding.root)
}
