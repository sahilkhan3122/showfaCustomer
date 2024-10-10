package ui.selectedAddress

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemSearchPlaceBinding
import model.SaveRecentPlayListModel

class SelectedRecentAdapter(
    var context: Context,
    var recentAddressList: ArrayList<SaveRecentPlayListModel.Data>,
    var callback:(position:Int)->Unit
) : RecyclerView.Adapter<SelectedRecentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchPlaceBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = recentAddressList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtCityName.text = recentAddressList[position].title
        holder.binding.currentCityAddress.text = recentAddressList[position].address

        holder.binding.txtCityName.setOnClickListener {
            callback.invoke(position)
        }

    }

    inner class MyViewHolder(var binding: ItemSearchPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)
}