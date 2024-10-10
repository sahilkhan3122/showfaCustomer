package model

data class WalletHistoryModel(
    val status: Boolean = false,
    val wallet_balance: Int = 0,
    val is_saving_wallet: String = "",
    val page: Int = 0,
    var `data`:ArrayList<Data>,
    val view_link: String = ""
) {
    data class Data(
        val id: String = "",
        val user_id: String = "",
        val user_type: String = "",
        val amount: String = "",
        val transaction_type: String = "",
        val payment_type: String = "",
        val type: String = "",
        val reference_id: String = "",
        val description: String = "",
        val created_by: String = "",
        val created_at: String = "",
        val status: String = "",
        val admin_paid: String = "",
        val debit_account: Any = Any()
    )
}