package com.example.accountapp

import ApiInterface
import LoginResponses
import RegisterResponses
import UserAccount.AccountActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.accountapp.databinding.FragmentRegisterBinding
import retrofit2.Callback
import org.json.JSONObject
import java.lang.Exception


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            if(checkFieldIsEmpty())
            {
                binding.textviewStatusmsg.text = "Please ensure all fields are filled"
                binding.textviewStatusmsg.visibility = View.VISIBLE
            }
            else if(!checkIsPasswordMatch())
            {
                binding.textviewStatusmsg.text = "Password Mismatch"
                binding.textviewStatusmsg.visibility = View.VISIBLE
            }
            else
            {
                binding.textviewStatusmsg.text = "Password Mismatch"
                binding.textviewStatusmsg.visibility = View.INVISIBLE
                binding.registerProgressBar.visibility = View.VISIBLE
                setupRegisterCallback()
            }
        }
    }

    private fun checkFieldIsEmpty(): Boolean
    {
        return(binding.editTextUsername.text.isBlank() ||
                binding.editTextPassword.text.isBlank() ||
                binding.editTextPassword2.text.isBlank() )
    }

    private fun checkIsPasswordMatch(): Boolean
    {
        return(binding.editTextPassword.text.toString().equals(binding.editTextPassword2.text.toString()))
    }

    fun setupRegisterCallback()
    {
        val api = ApiHelper.getInstance().create(ApiInterface::class.java)
        api.userRegister(binding.editTextUsername.text.toString(),
        binding.editTextPassword.text.toString())
            .enqueue(object: Callback<RegisterResponses> {
                override fun onFailure(call: retrofit2.Call<RegisterResponses>, t: Throwable) {
                    binding.textviewStatusmsg.text = t.message
                    binding.textviewStatusmsg.visibility = View.VISIBLE
                    binding.registerProgressBar.visibility = View.GONE
                }

                override fun onResponse(call: retrofit2.Call<RegisterResponses>, response: retrofit2.Response<RegisterResponses>) {
                    binding.registerProgressBar.visibility = View.GONE
                    if(response.body() != null)
                    {
                        if(response.body()?.status.equals("success", true))
                        {
                            binding.textviewStatusmsg.text = "User Account Created Successfully"
                        }
                        else
                        {
                            binding.textviewStatusmsg.text = response.body()?.status
                        }

                        binding.textviewStatusmsg.visibility = View.VISIBLE
                        binding.registerProgressBar.visibility = View.GONE
                    }
                    else
                    {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            binding.textviewStatusmsg.text = jObjError.getString("error")
                            binding.textviewStatusmsg.visibility = View.VISIBLE
                        } catch (e: Exception) {
                            e.message?.let { it1 -> Log.d("Register", it1) }
                        }
                    }
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}