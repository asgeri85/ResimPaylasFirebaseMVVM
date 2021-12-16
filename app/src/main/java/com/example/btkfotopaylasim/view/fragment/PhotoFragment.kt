package com.example.btkfotopaylasim.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.btkfotopaylasim.databinding.FragmentPhotoBinding
import com.example.btkfotopaylasim.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class PhotoFragment : Fragment() {
    private var _binding:FragmentPhotoBinding?=null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null
    var secilenGorsel:Uri?=null
    private val homeViewModel by lazy { HomeViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding= FragmentPhotoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonFotoSec.setOnClickListener {
                gorselSec()
            }
            buttonPaylas.setOnClickListener {
                val yorum=editAciklama.text.toString().trim()
                if (secilenGorsel!=null && yorum.isNotEmpty()){
                    homeViewModel.fotoYukle(secilenGorsel!!,yorum)
                }else{
                    Toast.makeText(context,"Görsel seçin",Toast.LENGTH_SHORT).show()
                }
            }
            observeLiveData()
            registerLauncher()
        }
    }

    private fun observeLiveData(){
        with(binding){
            with(homeViewModel){
                imageLoad.observe(viewLifecycleOwner,{
                    if (it){
                        Toast.makeText(context,"Fotoğraf yüklendi",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"Hata",Toast.LENGTH_SHORT).show()
                    }
                })

                loadingImage.observe(viewLifecycleOwner,{
                    if (it){
                        progressPhoto.visibility=View.VISIBLE
                    }else{
                        progressPhoto.visibility=View.INVISIBLE
                    }
                })
            }
        }
    }

    private fun gorselSec(){
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(requireView(), "İzin verilmedi", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin verin") {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            }else{
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    secilenGorsel = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, secilenGorsel!!)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageViewPhoto.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel!!)
                            binding.imageViewPhoto.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}