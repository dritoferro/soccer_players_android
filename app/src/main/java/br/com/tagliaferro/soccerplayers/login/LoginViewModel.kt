package br.com.tagliaferro.soccerplayers.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import br.com.tagliaferro.soccerplayers.integration.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.java.simpleName

    private val _isPressed = MutableLiveData<Boolean>()

    val isPressed: LiveData<Boolean>
        get() = _isPressed

    //TODO adicionar pe
    fun login(credentials: LoginDTO) {
        _isPressed.value = true
        Log.i(TAG, credentials.username.toString())
        Log.i(TAG, credentials.password.toString())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://spring-monolithic-stg.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LoginService::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            val loginUser = service.login(credentials)

            val result = loginUser.execute()

            Log.i(TAG, result.body().toString())
        }

    }

    init {
        Log.i(TAG, "LoginViewModel created!")

        _isPressed.value = false
    }

    fun isPressedComplete() {
        _isPressed.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "LoginViewModel destroyed!")
    }
}
