package model

data class SendWalletMoneyModel(
    val status: Boolean = false,
    val message: String = "",
    val wallet_balance: Int = 0
)