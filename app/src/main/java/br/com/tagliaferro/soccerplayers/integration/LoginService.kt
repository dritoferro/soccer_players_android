package br.com.tagliaferro.soccerplayers.integration

import br.com.tagliaferro.soccerplayers.entities.LoggedUserDTO
import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("login")
    fun login(@Body data: LoginDTO): Call<LoggedUserDTO>
}