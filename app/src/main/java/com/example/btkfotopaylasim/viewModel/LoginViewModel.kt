package com.example.btkfotopaylasim.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.btkfotopaylasim.model.Response
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel:ViewModel() {
    var isLogin=MutableLiveData<Response>()
    var loading=MutableLiveData<Boolean>()
    var isRegister=MutableLiveData<Response>()
    private val auth=FirebaseAuth.getInstance()

    fun register(email:String,password:String){
        loading.value=true
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                loading.value=false
                isRegister.value= Response("Kayıt işlemi başarılı",true)
            }else{
                loading.value=false
                task.exception?.let { e->
                    isRegister.value=Response(e.localizedMessage,false)
                }
            }
        }
    }

    fun login(email:String,pass:String){
        loading.value=true
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener { task->
            if (task.isSuccessful){
                loading.value=false
                isLogin.value=Response("Giriş başarılı",true)
            }else{
                loading.value=false
                task.exception?.let { it->
                    isLogin.value=Response(it.localizedMessage,false)
                }
            }
        }
    }

    fun exit(){
        isLogin.value=Response("Çıkış yapıldı",false)
        auth.signOut()
    }
}