package model

data class SocialLoginModel(
    val status: Boolean = false,
    val message: String = "",
    val invite_message: String = "",
    val sos_number: String = ""
)