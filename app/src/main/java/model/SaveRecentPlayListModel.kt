package model

data class
SaveRecentPlayListModel(
    var status: Boolean = false,
    var message: String = "",
    var data: ArrayList<Data>
) {
    data class Data(
        val id: String = "",
        val customer_id: String = "",
        val title: String = "",
        val address: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val created_date: String = ""
    )
}