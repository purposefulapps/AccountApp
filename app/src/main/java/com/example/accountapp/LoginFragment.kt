package com.example.accountapp

import ApiInterface
import LoginResponses
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.accountapp.databinding.FragmentLoginBinding
import retrofit2.Callback
import UserAccount.AccountActivity
import android.content.Intent


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            if(checkFieldIsEmpty())
            {
                binding.textviewErrormsg.text = "Please ensure all fields are filled"
                binding.textviewErrormsg.visibility = View.VISIBLE
            }
            else
            {
                binding.textviewErrormsg.text = ""
                binding.textviewErrormsg.visibility = View.INVISIBLE
                binding.loginProgressBar.visibility = View.VISIBLE
                setupLoginCallback()
            }
        }

        binding.buttonRegister.setOnClickListener{
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
    }

    private fun setupLoginCallback()
    {
        val api = ApiHelper.getInstance().create(ApiInterface::class.java)
        api.userLogin(binding.editTextUsername.text.toString(),
            binding.editTextPassword.text.toString())
            .enqueue(object: Callback<LoginResponses> {
                override fun onFailure(call: retrofit2.Call<LoginResponses>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    binding.loginProgressBar.visibility = View.GONE
                }

                override fun onResponse(call: retrofit2.Call<LoginResponses>, response: retrofit2.Response<LoginResponses>) {
                    if(response.body() != null)
                    {
                        UserData.token = response.body()?.token.toString()
                        UserData.username = response.body()?.username.toString()
                        UserData.accountNo = response.body()?.accountNo.toString()
                        val accountIntent = Intent(context, AccountActivity::class.java)
                        startActivity(accountIntent)
                        binding.loginProgressBar.visibility = View.GONE
                    }
                    else
                    {
                        if(response.message().equals("unauthorized", true) ||
                            response.message().equals("not found", true) )
                        {
                            binding.textviewErrormsg.text = "User Not Found, or Invalid Password"
                            binding.textviewErrormsg.visibility = View.VISIBLE
                        }

                        Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                        binding.loginProgressBar.visibility = View.GONE
                    }
                }
            })
    }

    private fun checkFieldIsEmpty(): Boolean
    {
        return(binding.editTextUsername.text.isNullOrBlank() ||
                binding.editTextPassword.text.isNullOrBlank())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}