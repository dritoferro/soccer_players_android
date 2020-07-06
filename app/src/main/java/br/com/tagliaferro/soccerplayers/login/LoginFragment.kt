package br.com.tagliaferro.soccerplayers.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.tagliaferro.soccerplayers.databinding.LoginFragmentBinding
import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import br.com.tagliaferro.soccerplayers.integration.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {

    private val TAG = LoginFragment::class.java.simpleName

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener { login() }

        return binding.root
    }

    fun login() {
        val user = binding.edtUser.text.toString()
        val password = binding.edtPassword.text.toString()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://spring-monolithic-stg.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LoginService::class.java)

        val teste = service.login(LoginDTO(user, password))

        val answer = teste.execute()

        Log.i(TAG, answer.isSuccessful.toString())
        Log.i(TAG, answer.message())
    }
}