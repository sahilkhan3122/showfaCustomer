package model

data class DownloadInvoiceModel(
    val status: Boolean = false,
    val message: String = "",
    val `data`: String = ""
)