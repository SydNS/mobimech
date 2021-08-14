import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobimech.R
import com.example.mobimech.models.ServicesModel
import java.util.*

class ServiceListAdapter(
    var serviceArrayList: ArrayList<ServicesModel>,
    var  mListener: OnItemClickListener
) :
    RecyclerView.Adapter<ServiceListAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.services_viewholder, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // Set the data to textview and imageview.
        val recyclerData: ServicesModel = serviceArrayList[position]
        holder.serviceTv.text = recyclerData.title
        holder.serviceIv.setImageResource(recyclerData.imgid)
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return serviceArrayList.size
    }

    // View Holder Class to handle Recycler View.
    inner class RecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val serviceTv: TextView = itemView.findViewById(R.id.idTVService)
        var serviceIv: ImageView = itemView.findViewById(R.id.idIVServiceIV)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                mListener.onItemClick(position)
            }
        }

    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        if (listener != null) mListener = listener
    }


}