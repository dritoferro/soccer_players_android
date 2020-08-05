package br.com.tagliaferro.soccerplayers.repositories

import br.com.tagliaferro.soccerplayers.entities.CreateUserDTO
import br.com.tagliaferro.soccerplayers.entities.ResultDTO
import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO
import br.com.tagliaferro.soccerplayers.integration.RestClient
import br.com.tagliaferro.soccerplayers.integration.UserService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val converter = Gson()

    suspend fun createUser(user: CreateUserDTO): ResultDTO {
        val retrofit = RestClient.retrofit()

        val client = retrofit.create(UserService::class.java)

        val promise = GlobalScope.async(Dispatchers.IO) {
            client.createUser(user).execute()
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

        return ResultDTO(body = result.body(), error = error)
    }
}