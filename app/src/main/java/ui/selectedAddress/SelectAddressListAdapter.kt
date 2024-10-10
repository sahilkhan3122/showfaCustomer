package ui.selectedAddress

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemSearchPlaceBinding
import model.ListClass

class SelectAddressListAdapter(
    var context: Context,
    private var list: ArrayList<ListClass>?, var callback: (Position: Int) -> Unit
) : RecyclerView.Adapter<SelectAddressListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchPlaceBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtCityName.setText(list?.get(position)?.structuredFormatting?.main_text )
        holder.binding.currentCityAddress.setText(list?.get(position)?.description)


        Log.d("DATA","structureformating----->${list?.get(position)?.structuredFormatting?.main_text}")
        Log.d("DATA","description----->${list?.get(position)?.description}")

        holder.binding.constraint.setOnClickListener {
            callback.invoke(position)
        }

    }
    inner class MyViewHolder(var binding: ItemSearchPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)
}