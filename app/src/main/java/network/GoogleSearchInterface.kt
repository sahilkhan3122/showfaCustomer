package network

import model.MainPojo
import model.PlaceIdLatLngModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface GoogleSearchInterface {
    @GET("place/autocomplete/json")
    fun getSearch(
        @Query("input") text: String?,
        @Query("components") country: String?,
        @Query("key") key: String?
    ): Call<MainPojo?>?

    @GET
    fun getLatlng(
        @Url url:String
    ): Call<PlaceIdLatLngModel>
}
