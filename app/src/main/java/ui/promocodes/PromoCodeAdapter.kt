package ui.promocodes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemPromoCodesBinding
import model.PromocodsModel

class PromoCodeAdapter(
    var context: Context, var promoCodesModel: ArrayList<PromocodsModel.Data>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<PromoCodeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPromoCodesBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentPosition = promoCodesModel[position]
        holder.binding.promoCodes.text = currentPosition.promocode
        holder.binding.validTill.text = currentPosition.end_date

    }

    override fun getItemCount(): Int = promoCodesModel!!.size

    inner class MyViewHolder(var binding: ItemPromoCodesBinding) :
        RecyclerView.ViewHolder(binding.root)
}
