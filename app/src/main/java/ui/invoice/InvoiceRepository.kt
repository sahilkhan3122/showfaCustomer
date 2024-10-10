package ui.invoice

import model.DownloadInvoiceModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class InvoiceRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun getInvoiceRepository(): Response<DownloadInvoiceModel> {
        return apiInterface.downloadInvoiceApi()

    }
}