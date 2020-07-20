package br.com.tagliaferro.soccerplayers.entities

data class CreateUserDTO(
    var nome: String? = null,

    var apelido: String? = null,

    var email: String? = null,

    var senha: String? = null
)
