package UserAccount

import Model.TransactionChildItemModel
import Model.TransactionParentItemModel
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.accountapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import java.security.AccessController.getContext


class TransactionParentItemAdapter(private val mList: MutableList<TransactionParentItemModel>) :
            RecyclerView.Adapter<TransactionParentItemAdapter.ViewHolder>()
{
    private val viewPool = RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_parent_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        val layoutManager = LinearLayoutManager(
            holder.recyclerview_child.getContext(),
            LinearLayoutManager.VERTICAL,
            false)

        layoutManager.initialPrefetchItemCount = ItemsViewModel.recipientList.size
        val childAdapter = TransactionChildItemAdapter(ItemsViewModel.recipientList)
        holder.recyclerview_child.layoutManager = layoutManager
        holder.recyclerview_child.adapter = childAdapter
        holder.recyclerview_child.setRecycledViewPool(viewPool)
        holder.textviewDate.text = ItemsViewModel.date
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val recyclerview_child: RecyclerView = itemView.findViewById(R.id.child_item_recyclerview)
        val textviewDate: TextView = itemView.findViewById(R.id.parent_item_date)
    }
}