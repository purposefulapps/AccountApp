package UserAccount

import Model.TransactionChildItemModel
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.accountapp.R

class TransactionChildItemAdapter(private val mList: List<TransactionChildItemModel>) :
            RecyclerView.Adapter<TransactionChildItemAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_child_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.textviewName.text = ItemsViewModel.name
        holder.textviewAccountNo.text = ItemsViewModel.accountNo
        holder.textviewAmount.text = ItemsViewModel.amount.toString()
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textviewName: TextView = itemView.findViewById(R.id.child_item_name)
        val textviewAccountNo: TextView = itemView.findViewById(R.id.child_item_accountNo)
        val textviewAmount: TextView = itemView.findViewById(R.id.child_item_amount)
    }
}