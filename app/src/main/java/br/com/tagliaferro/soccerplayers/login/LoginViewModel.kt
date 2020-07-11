package br.com.tagliaferro.soccerplayers.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO
import br.com.tagliaferro.soccerplayers.integration.LoginService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {

    private val TAG = LoginViewModel::class.java.simpleName

    private val BASE_URL = "https://spring-monolithic-stg.herokuapp.com/api/v1/"

    private val converter = Gson()

    private val _isPressed = MutableLiveData<Boolean>()

    val isPressed: LiveData<Boolean>
        get() = _isPressed

    private val _error = MutableLiveData<ErrorDTO>()

    val error: LiveData<ErrorDTO>
        get() = _error

    fun login(credentials: LoginDTO) {
        _isPressed.value = true
        Log.i(TAG, credentials.username.toString())
        Log.i(TAG, credentials.password.toString())

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(LoginService::class.java)

        viewModelScope.launch(Dispatchers.IO) {
            val loginResult = service.login(credentials).execute()

            if (loginResult.isSuccessful && loginResult.body() != null) {
                //TODO salvar o body no cache
            } else {
                val error = converter.fromJson<ErrorDTO>(
                    loginResult.errorBody()?.string(),
                    ErrorDTO::class.java
                )
                withContext(Dispatchers.Main) {
                    loginError(error)
                }
            }
        }
    }

    fun loginError(errorDTO: ErrorDTO) {
        _error.value = errorDTO
    }

    init {
        Log.i(TAG, "LoginViewModel created!")

        _isPressed.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "LoginViewModel destroyed!")
    }
}
