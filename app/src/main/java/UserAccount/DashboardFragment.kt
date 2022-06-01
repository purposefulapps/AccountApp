package UserAccount

import ApiHelper
import ApiInterface
import Balance
import Model.TransactionChildItemModel
import Model.TransactionParentItemModel
import Payees
import Transactions
import UserData
import android.content.Intent
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.HomeActivity
import com.example.home.R
import com.example.home.databinding.FragmentDashboardBinding
import retrofit2.Callback


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    val hashMapTransactions = HashMap<String, ArrayList<TransactionChildItemModel>>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataFetchCallbacks()

        binding.textviewLogout.setOnClickListener{
            val accountIntent = Intent(context, HomeActivity::class.java)
            startActivity(accountIntent)
        }
        val layoutManager = LinearLayoutManager(context)
        binding.transactionList.layoutManager = layoutManager

        binding.buttonTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_AccountFragment_to_TransferFragment)
        }
    }

    fun setupDataFetchCallbacks()
    {
        val api = ApiHelper.getInstance().create(ApiInterface::class.java)

        api.getBalance(UserData.token).enqueue(object: Callback<Balance> {
            override fun onFailure(call: retrofit2.Call<Balance>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<Balance>, response: retrofit2.Response<Balance>) {
                if(response.body() != null)
                {
                    Toast.makeText(context, response.body()?.status, Toast.LENGTH_LONG).show()
                    updateSummaryWidget(response.body()?.accountNo, response.body()?.balance)
                }
                else
                {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                }
            }
        })

        api.getTransactions(UserData.token).enqueue(object: Callback<Transactions> {
            override fun onFailure(call: retrofit2.Call<Transactions>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<Transactions>, response: retrofit2.Response<Transactions>) {
                if(response.body() != null && response.body()?.status.equals("success", true))
                {
                    storeTransactions(response.body()?.data)
                    val adapter = TransactionParentItemAdapter(convertToModel(hashMapTransactions));
                    binding.transactionList.adapter = adapter
                }
                else
                {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun updateSummaryWidget(accountNo: String?, balance: Double?)
    {
        if((accountNo != null) && (balance != null))
        {
            if(accountNo.equals(UserData.accountNo, true))
            {
                binding.textviewBalance.setText(formatCurrency(balance))
                binding.textviewAccountNo.setText(accountNo.toString())
                binding.textviewAccountHolder.setText(UserData.username)
            }
        }
        else
        {
            Toast.makeText(context, "Server busy, please try login later.", Toast.LENGTH_LONG).show()
        }
    }

    fun storeTransactions(data: List<Transactions.TransactionDetails>?)
    {
        if(data != null)
        {
            hashMapTransactions.clear()

            for(item in data)
            {
                val accountHolder = if(item.receipient != null) item.receipient.accountHolder else "Default"
                val accountNo = if(item.receipient != null) item.receipient.accountNo else "--"
                val amount = if(item.amount != null) item.amount else 0.00
                val transactionDate = if(item.transactionDate != null) item.transactionDate else ""

                var record = TransactionChildItemModel(accountHolder, accountNo, formatCurrency(amount))
                var date = formatDateTime(transactionDate)
                if(hashMapTransactions.containsKey(date))
                {
                    val arrayList = hashMapTransactions[date]
                    arrayList!!.add(record)
                    hashMapTransactions[date] = arrayList
                }
                else
                {
                    val arrayList = ArrayList<TransactionChildItemModel>()
                    arrayList.add(record)
                    hashMapTransactions.put(date, arrayList)
                }
            }
        }
    }

    fun convertToModel(transactionHashMap: HashMap<String, ArrayList<TransactionChildItemModel>>)
                            : MutableList<TransactionParentItemModel>
    {
        var list = mutableListOf<TransactionParentItemModel>()

        for(item  in transactionHashMap)
        {
            var record = TransactionParentItemModel(item.key, item.value)
            list.add(record)
        }
        list.sortByDescending { it.date }
        return list
    }

    fun formatDateTime(datetime: String): String
    {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formateStyle8 = "dd-MMM-yyyyThh:mm:ss"
        val formatter = SimpleDateFormat("dd MMM yyyy")
        var output = formatter.format(parser.parse(datetime))

        return output
    }

    fun formatCurrency(value: Double): String
    {
        val formatter: NumberFormat = DecimalFormat("###,###,##0.00")
        formatter.setMaximumFractionDigits(2);
        val formattedValue = formatter.format(value)

        return formattedValue
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}