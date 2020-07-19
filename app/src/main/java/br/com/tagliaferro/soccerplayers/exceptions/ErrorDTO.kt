package br.com.tagliaferro.soccerplayers.exceptions

data class ErrorDTO(
    val status: String? = null,

    val message: String? = null,

    val timestamp: Long? = null,

    val subErrors: List<Any>? = null
)
