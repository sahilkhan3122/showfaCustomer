package ui.splashscreen

import model.SplashModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

open class SplashscreenRepository @Inject constructor(var apiInterface:ApiInterface) {

    suspend fun getRepository(): Response<SplashModel> {
        return apiInterface.initApiCall()
    }

}