package br.com.tagliaferro.soccerplayers.entities

import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO

data class ResultDTO(
    val body: Any? = null,

    val error: ErrorDTO? = null
)
