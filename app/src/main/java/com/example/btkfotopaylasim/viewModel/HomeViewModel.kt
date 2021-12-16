package com.example.btkfotopaylasim.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.btkfotopaylasim.model.Post
import com.example.btkfotopaylasim.model.Response
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeViewModel:ViewModel() {
    private val storage:FirebaseStorage= FirebaseStorage.getInstance()
    private val auth:FirebaseAuth= FirebaseAuth.getInstance()
    private val fireStore:FirebaseFirestore= FirebaseFirestore.getInstance()
    val imageLoad=MutableLiveData<Boolean>()
    private var veriler=ArrayList<Post>()
    val loadingImage=MutableLiveData<Boolean>()
    val postlar=MutableLiveData<List<Post>>()
    val veriLoading=MutableLiveData<Boolean>()
    val postError=MutableLiveData<Response>()

    fun fotoYukle(uri:Uri,text:String){
        loadingImage.value=true
        val uuid=UUID.randomUUID()
        val imageName="${uuid}.jpg"
        storage.reference.child("images").child(imageName).putFile(uri).addOnCompleteListener {
            if (it.isSuccessful){
                storage.reference.child("images").child(imageName).downloadUrl.addOnSuccessListener {link->
                    val url=link.toString()
                    val mail=auth.currentUser!!.email.toString()
                    val tarih=Timestamp.now()

                    val postHashmap=HashMap<String,Any>()
                    postHashmap["url"] = url
                    postHashmap["mail"]=mail
                    postHashmap["tarih"]=tarih
                    postHashmap["yorum"]=text

                    fireStore.collection("Post").add(postHashmap).addOnCompleteListener { task->
                        if (task.isSuccessful){
                            loadingImage.value=false
                            imageLoad.value=true
                        }
                    }
                }
            }
        }
    }

    fun veriGetir(){
        veriLoading.value=true
        fireStore.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error==null){
                if (value!=null){
                    if (!value.isEmpty){
                        veriler.clear()
                        for (d in value.documents){
                            val  mail=d.get("mail") as String
                            val url=d.get("url") as String
                            val yorum=d.get("yorum") as String
                            val post=Post(mail,url,yorum)
                            veriler.add(post)
                        }
                        postlar.value=veriler
                        veriLoading.value=false
                    }
                }
            }else{
                postError.value= Response(error.localizedMessage.toString(),true)
                veriLoading.value=false
            }
        }
    }


}