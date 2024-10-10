package ui.wallet

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemWalletHistoryBinding
import model.WalletHistoryModel

class WalletAdapter(
    var context: Context, var previousDueModel: ArrayList<WalletHistoryModel.Data>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<WalletAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemWalletHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPosition = previousDueModel[position]
        holder.binding.pastPaymentBooking.text = currentPosition.description
        holder.binding.txtBookingId.text = "Booking ID:" + currentPosition.id
        holder.binding.txtBookingPastAndReceived.text = currentPosition.amount
        holder.binding.txtDateAndTime.text = currentPosition.created_at
        Log.d("TAG", "AMOUNT...${currentPosition.amount}")

    }

    override fun getItemCount(): Int = previousDueModel!!.size

    inner class MyViewHolder(var binding: ItemWalletHistoryBinding) :

        RecyclerView.ViewHolder(binding.root)
}
