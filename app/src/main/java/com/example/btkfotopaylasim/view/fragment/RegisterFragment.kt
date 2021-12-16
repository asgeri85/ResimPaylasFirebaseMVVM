package com.example.btkfotopaylasim.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.btkfotopaylasim.R
import com.example.btkfotopaylasim.databinding.FragmentRegisterBinding
import com.example.btkfotopaylasim.viewModel.LoginViewModel

class RegisterFragment : Fragment() {
    private var _binding:FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    private val viewModel by lazy { LoginViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentRegisterBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonSignup.setOnClickListener {
                val mail=editRegisterMail.text.toString().trim()
                val pass=editRegisterPass.text.toString().trim()
                if (mail.isNotEmpty() && pass.isNotEmpty()){
                    viewModel.register(mail,pass)
                }else{
                    Toast.makeText(context,"Tüm alanları doldurunuz",Toast.LENGTH_LONG).show()
                }
            }
        }
        observeLiveData()
    }

    private fun observeLiveData(){
        with(binding) {
            with(viewModel) {
                isRegister.observe(viewLifecycleOwner, {
                    if (it.tf){
                        Toast.makeText(context,it.text, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context,it.text, Toast.LENGTH_LONG).show()
                    }
                })

                loading.observe(viewLifecycleOwner,{
                    if (it){
                        progressRegister.visibility=View.VISIBLE
                    }else{
                        progressRegister.visibility=View.GONE
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