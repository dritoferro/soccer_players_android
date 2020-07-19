package br.com.tagliaferro.soccerplayers.entities

import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO

data class LoginResponseDTO(
    val user: LoggedUserDTO? = null,

    val error: ErrorDTO? = null
)
