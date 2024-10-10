package model

data class WalletAddMoneyModel(
    var status: Boolean = false,
    var message: String = "",
    var payment_url: String = ""
)