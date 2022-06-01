package UserAccount

import ApiInterface
import Payees
import TransferResponses
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.home.R
import com.example.home.databinding.FragmentTransferBinding
import retrofit2.Callback
import android.widget.AdapterView.OnItemClickListener


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TransferFragment : Fragment() {

    private var _binding: FragmentTransferBinding? = null
    private var hashMapPayees = HashMap<String, String>()
    private var nameList : MutableList<String> = arrayListOf()
    lateinit var adapter : ArrayAdapter<String>
    val api = ApiHelper.getInstance().create(ApiInterface::class.java)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = context!!.let { ArrayAdapter(it, R.layout.list_item, nameList) }
        binding.spinnerPayee.setAdapter(adapter)
        binding.spinnerPayee.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String? = adapter?.getItem(position)
                binding.textviewAccountno.text = getPayeeAccountNo(selectedValue.toString())
                binding.textviewAccountno.visibility = View.VISIBLE
            }

        setupPayeeCallback();
        binding.buttonTransfer.setOnClickListener {
            setupTransferCallback()
        }
    }

    fun setupPayeeCallback()
    {
        hashMapPayees.clear()

        api.getPayees(UserData.token).enqueue(object: Callback<Payees> {
            override fun onFailure(call: retrofit2.Call<Payees>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<Payees>, response: retrofit2.Response<Payees>) {
                if(response.body() != null)
                {
                    hashMapPayees.clear()
                    nameList.clear()
                    val payeeList: List<Payees.Payee>? = response.body()?.data
                    if(payeeList != null)
                    {
                        for(payee in payeeList)
                        {
                            hashMapPayees.put(payee.name, payee.accountNo)
                            nameList.add(payee.name)
                        }

                        adapter!!.notifyDataSetChanged()
                    }
                }
                else
                {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun setupTransferCallback()
    {
        var amount: Double = 0.0

        try{
            amount = binding.editTextAmount.text.toString().toDouble()

            api.postTransfer(UserData.token,
                binding.textviewAccountno.text.toString(),
                amount,
                binding.editTextDescription.text.toString()).
                enqueue(object: Callback<TransferResponses> {
                override fun onFailure(call: retrofit2.Call<TransferResponses>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: retrofit2.Call<TransferResponses>, response: retrofit2.Response<TransferResponses>) {
                    if(response.body() != null)
                    {
                        Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        catch (e: Exception)
        {
            Toast.makeText(context, "Please key in valid numbers for Amount", Toast.LENGTH_LONG).show()
        }
    }

    fun getPayeeAccountNo(name: String): String
    {
        var accountno: String = ""
        if(hashMapPayees.containsKey(name))
        {
            accountno = hashMapPayees[name]!!
        }

        return accountno
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}