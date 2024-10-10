package model

data class ResendOTPModel(
    val status: Boolean = false,
    val otp: Int = 0,
    val message: String = ""
)