package ui.savedPlaces

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ItemSavedPlacesBinding
import model.FavAddressListModel

class SavedPlacesAdapter(
    var context: Context,
    var saveAddressList: ArrayList<FavAddressListModel.Data>,
    var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<SavedPlacesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSavedPlacesBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = saveAddressList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtLocation.text = saveAddressList[position].pickupLocation
        if (saveAddressList[position].favouriteType=="home"){
            Glide.with(context).load(saveAddressList[position].favouriteType).placeholder(R.drawable.ic_black_home).into(holder.binding.imgHome)
        }else if (saveAddressList[position].favouriteType=="office"){
            Glide.with(context).load(saveAddressList[position].favouriteType).placeholder(R.drawable.ic_office).into(holder.binding.imgHome)
        }else if (saveAddressList[position].favouriteType=="other"){
            Glide.with(context).load(saveAddressList[position].favouriteType).placeholder(R.drawable.ic_my_location).into(holder.binding.imgHome)
        }
        holder.binding.constrainItemAddress.setOnClickListener {
            callback.invoke(position)
        }


    }
    inner class MyViewHolder(var binding: ItemSavedPlacesBinding) :
        RecyclerView.ViewHolder(binding.root)
}