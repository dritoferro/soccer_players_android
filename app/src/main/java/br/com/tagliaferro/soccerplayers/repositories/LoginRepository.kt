package br.com.tagliaferro.soccerplayers.repositories

import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import br.com.tagliaferro.soccerplayers.entities.LoginResponseDTO
import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO
import br.com.tagliaferro.soccerplayers.integration.LoginService
import br.com.tagliaferro.soccerplayers.integration.RestClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor() {
    private val converter = Gson()

    suspend fun executeLogin(credentials: LoginDTO): LoginResponseDTO {
        val retrofit = RestClient.retrofit()

        val service = retrofit.create(LoginService::class.java)

        val promise = GlobalScope.async(Dispatchers.IO) {
            service.login(credentials).execute()
        }

        val result = promise.await()

        val error = withContext(Dispatchers.IO) {
            if (result.errorBody() != null) {
                converter.fromJson<ErrorDTO>(
                    result.errorBody()!!.string(),
                    ErrorDTO::class.java
                )
            } else {
                null
            }
        }

        return LoginResponseDTO(user = result.body(), error = error)
    }
}