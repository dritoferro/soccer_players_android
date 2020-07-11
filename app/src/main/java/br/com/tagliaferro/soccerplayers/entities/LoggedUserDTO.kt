package br.com.tagliaferro.soccerplayers.entities

data class LoggedUserDTO(
    val id: Int? = null,

    val nome: String? = null,

    val email: String? = null,

    val apelido: String? = null,

    val bloqueado: Boolean? = null,

    val token: String? = null,

    val tokenExpiration: Long? = null
)
