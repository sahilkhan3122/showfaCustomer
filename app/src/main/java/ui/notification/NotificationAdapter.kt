package ui.notification

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showfa_customer_android.databinding.ItemNotificationBinding
import model.NotificationListModel

class NotificationAdapter(var context: Context, var notificationModel: ArrayList<NotificationListModel.Data>,
                          var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      var currentPosition=notificationModel[position]

            holder.binding.driverHaseArrived.text=currentPosition.title
            holder.binding.txtTime.text=currentPosition.createdDate
        Log.d("DATA","data===>${currentPosition.title}")
        Log.d("DATA","currentPosition.created_date===>${currentPosition.createdDate}")

    }

    override fun getItemCount(): Int = notificationModel!!.size

    inner class MyViewHolder(var binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)
}
