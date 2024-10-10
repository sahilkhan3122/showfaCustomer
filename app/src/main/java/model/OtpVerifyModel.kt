package model

data class OtpVerifyModel(
    val status: Boolean = false,
    val otp: Int?=null,
    val message: String = ""
)