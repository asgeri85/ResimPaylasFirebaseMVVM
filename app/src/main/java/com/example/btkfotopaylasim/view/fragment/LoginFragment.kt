package com.example.btkfotopaylasim.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.btkfotopaylasim.databinding.FragmentLoginBinding
import com.example.btkfotopaylasim.view.activties.MainActivity
import com.example.btkfotopaylasim.viewModel.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding:FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private val viewModel by lazy { LoginViewModel() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonLogin.setOnClickListener {
                val mail=editLoginMail.text.toString().trim()
                val pass=editLoginPass.text.toString().trim()
                if (mail.isNotEmpty() && pass.isNotEmpty()){
                    viewModel.login(mail,pass)
                }else{
                    Toast.makeText(context,"Tüm alanları doldurun",Toast.LENGTH_SHORT).show()
                }
            }
            buttonRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            observeLiveData()
        }
    }

    private fun observeLiveData(){
        with(binding){
            with(viewModel){
                isLogin.observe(viewLifecycleOwner,{
                    if (it.tf){
                        startActivity(Intent(requireActivity(),MainActivity::class.java))
                        requireActivity().finish()
                        Toast.makeText(context,it.text,Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context,it.text,Toast.LENGTH_LONG).show()
                    }
                })

                loading.observe(viewLifecycleOwner,{
                    if (it){
                        progressLogin.visibility=View.VISIBLE
                    }else{
                        progressLogin.visibility=View.GONE
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}