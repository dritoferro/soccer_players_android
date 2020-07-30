package br.com.tagliaferro.soccerplayers.screens.login

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tagliaferro.soccerplayers.entities.LoggedUserDTO
import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO
import br.com.tagliaferro.soccerplayers.repositories.LoginRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel @ViewModelInject constructor(private val repository: LoginRepository) :
    ViewModel() {

    lateinit var preferencesKey: String

    lateinit var preferences: SharedPreferences

    private val converter = Gson()

    private val _isPressed = MutableLiveData<Boolean>()

    val isPressed: LiveData<Boolean>
        get() = _isPressed

    private val _error = MutableLiveData<ErrorDTO>()

    val error: LiveData<ErrorDTO>
        get() = _error

    private val _success = MutableLiveData<String>()

    val success: LiveData<String>
        get() = _success

    init {
        _isPressed.value = false
    }

    fun login(credentials: LoginDTO) {
        _isPressed.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginResult = repository.executeLogin(credentials)

                if (loginResult.user != null) {
                    val serialized = converter.toJson(loginResult.user)
                    preferences.edit().putString("$preferencesKey-login", serialized).apply()

                    withContext(Dispatchers.Main) {
                        onSuccess(loginResult.user)
                    }

                } else if (loginResult.error != null) {
                    withContext(Dispatchers.Main) {
                        loginError(loginResult.error)
                    }
                }
            } catch (ex: Exception) {
                loginError(ErrorDTO(status = "Unmapped Error", message = ex.message))
            }
        }
    }

    private fun loginError(errorDTO: ErrorDTO) {
        _error.value = errorDTO
    }

    private fun onSuccess(user: LoggedUserDTO) {
        _success.value = user.nome
    }
}
