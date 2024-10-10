package ui.promocodes

import model.PromocodsModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class PromoCodeRepository  @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun promoCodeRepository(): Response<PromocodsModel> {
        return apiInterface.promoCodesApi()
    }
}