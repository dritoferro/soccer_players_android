package br.com.tagliaferro.soccerplayers.screens.login

import android.content.SharedPreferences
import android.os.Build
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
import org.threeten.bp.ZoneId
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class LoginViewModel @ViewModelInject constructor(private val repository: LoginRepository) :
    ViewModel() {

    val preferencesKey = "SoccerPlayers"

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

    fun checkCache() {
        val isLogged = preferences.getString("$preferencesKey-login", null)

        if (!isLogged.isNullOrBlank()) {
            val user = converter.fromJson<LoggedUserDTO>(isLogged, LoggedUserDTO::class.java)

            val isValid = validateToken(user.tokenExpiration!!)

            if (isValid) {
                //TODO redirecionar para a próxima tela
                onSuccess(user)
            } else {
                //TODO redirecionar para o login
                loginError(ErrorDTO(message = "Usuário não logado"))
            }
        }
    }

    fun login(credentials: LoginDTO) {
        _isPressed.value = true

        viewModelScope.launch(Dispatchers.IO) {
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
        }
    }

    private fun validateToken(tokenExpiration: Long): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDateTime.now()
            val expiration = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(tokenExpiration),
                TimeZone.getDefault().toZoneId()
            )

            if (now.isAfter(expiration)) {
                preferences.edit().remove("$preferencesKey-login").apply()
                false
            } else {
                true
            }

        } else {
            val now = org.threeten.bp.LocalDateTime.now()
            val expiration = org.threeten.bp.LocalDateTime.ofInstant(
                org.threeten.bp.Instant.ofEpochMilli(tokenExpiration),
                ZoneId.systemDefault()
            )

            if (now.isAfter(expiration)) {
                preferences.edit().remove("$preferencesKey-login").apply()
                false
            } else {
                true
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
