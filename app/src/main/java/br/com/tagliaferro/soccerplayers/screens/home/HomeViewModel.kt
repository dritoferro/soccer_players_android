package br.com.tagliaferro.soccerplayers.screens.home

import android.content.SharedPreferences
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.tagliaferro.soccerplayers.entities.LoggedUserDTO
import com.google.gson.Gson
import org.threeten.bp.ZoneId
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class HomeViewModel @ViewModelInject constructor() : ViewModel() {

    lateinit var preferencesKey: String

    lateinit var preferences: SharedPreferences

    private val converter = Gson()

    private val _isAlreadyLogged = MutableLiveData<Boolean>()

    val isAlreadyLogged: LiveData<Boolean>
        get() = _isAlreadyLogged

    init {
        _isAlreadyLogged.value = false
    }

    fun checkCache() {

        val isLogged = preferences.getString("$preferencesKey-login", null)

        if (!isLogged.isNullOrBlank()) {
            val user = converter.fromJson<LoggedUserDTO>(isLogged, LoggedUserDTO::class.java)

            val isValid = validateToken(user.tokenExpiration!!)

            if (isValid) {
                _isAlreadyLogged.value = true
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
}