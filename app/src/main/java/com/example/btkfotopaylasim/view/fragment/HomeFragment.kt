package com.example.btkfotopaylasim.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btkfotopaylasim.R
import com.example.btkfotopaylasim.adapters.PostAdapter
import com.example.btkfotopaylasim.databinding.FragmentHomeBinding
import com.example.btkfotopaylasim.view.activties.MainActivity2
import com.example.btkfotopaylasim.viewModel.HomeViewModel
import com.example.btkfotopaylasim.viewModel.LoginViewModel

class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val loginModel by lazy { LoginViewModel() }
    private val homeViewModel by lazy { HomeViewModel() }
    private lateinit var adapter:PostAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter= PostAdapter(requireContext(), arrayListOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            rvHome.layoutManager=LinearLayoutManager(context)
            rvHome.setHasFixedSize(true)
            rvHome.adapter=adapter
            toolbarHome.title="Resimler"
            (activity as AppCompatActivity).setSupportActionBar(toolbarHome)
        }
        homeViewModel.veriGetir()
        setHasOptionsMenu(true)
        observeLiveData()
    }

    private fun observeLiveData(){
        with(binding){
            loginModel.isLogin.observe(viewLifecycleOwner,{
                if (!it.tf){
                    Toast.makeText(context,it.text,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(),MainActivity2::class.java))
                    requireActivity().finish()
                }
            })
            with(homeViewModel){
                postlar.observe(viewLifecycleOwner,{
                    it?.let {
                        rvHome.visibility=View.INVISIBLE
                        adapter.updateList(it)
                    }
                })

                veriLoading.observe(viewLifecycleOwner,{
                    if (it){
                        progressHome.visibility=View.INVISIBLE
                        rvHome.visibility=View.GONE
                    }else{
                        progressHome.visibility=View.GONE
                        rvHome.visibility=View.VISIBLE
                    }
                })
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_cikis-> {
                loginModel.exit()
                true
            }

            R.id.action_ekle->{
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPhotoFragment())
                 true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}