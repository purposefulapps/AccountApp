package UserAccount

import ApiInterface
import Balance
import Model.TransactionChildItemModel
import Model.TransactionParentItemModel
import Payees
import Transactions
import UserData
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.accountapp.R
import com.example.accountapp.databinding.FragmentDashboardBinding
import retrofit2.Callback

import androidx.recyclerview.widget.LinearLayoutManager




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

        api.getPayees(UserData.token).enqueue(object: Callback<Payees> {
            override fun onFailure(call: retrofit2.Call<Payees>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<Payees>, response: retrofit2.Response<Payees>) {
                if(response.body() != null)
                {
                    Toast.makeText(context, response.body()?.status, Toast.LENGTH_LONG).show()
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
                    Toast.makeText(context, response.body()?.status, Toast.LENGTH_LONG).show()
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
                binding.textviewBalance.setText(balance.toString())
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
                var record = TransactionChildItemModel(item.receipient.accountHolder,
                    item.receipient.accountNo,
                    item.amount)
                var date = formatDateTime(item.transactionDate)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}