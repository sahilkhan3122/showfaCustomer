package model

import com.google.gson.annotations.SerializedName

data class CheckMobileNumberCustomerDriverModel(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("total_receipt") val totalReceipt: Int = 0,
    @SerializedName("receipt_type") val receiptType: Int = 0
)