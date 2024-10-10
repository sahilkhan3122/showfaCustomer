package model

data class PromocodsModel(
    val status: Boolean = false,
    val `data`: ArrayList<Data>,
    val message: String = ""
) {
    data class Data(
        val id: String = "",
        val promocode: String = "",
        val promocode_type: String = "",
        val discount_type: String = "",
        val discount_value: String = "",
        val start_date: String = "",
        val end_date: String = "",
        val promocode_use_limit: String = "",
        val promocode_max_discount: String = "",
        val status: String = "",
        val trash: String = "",
        val created_at: String = ""
    )
}