package ui.tripHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemCancellationDialogBinding
import model.CancellationModel

class CancellationAdapter(
    var context: Context,
    var cancellationList: ArrayList<CancellationModel>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<CancellationAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCancellationDialogBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = cancellationList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val currentPosition=cancellationList[position]
        holder.binding.txtDriverDelay.text=currentPosition.textCancellationName
        holder.binding.checkbox.setOnClickListener {
                 callback.invoke(position)
        }
    }
    inner class MyViewHolder(var binding: ItemCancellationDialogBinding) :
        RecyclerView.ViewHolder(binding.root)
}