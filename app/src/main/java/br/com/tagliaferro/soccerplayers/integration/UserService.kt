package br.com.tagliaferro.soccerplayers.integration

import br.com.tagliaferro.soccerplayers.entities.CreateUserDTO
import br.com.tagliaferro.soccerplayers.entities.SimpleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("usuarios")
    fun createUser(@Body user: CreateUserDTO): Call<SimpleResponse>
}
