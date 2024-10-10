package di

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import laungage.MyDataStore
import network.ApiInterface
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.API_CONSTANTS.URL
import utils.RequestCodeCheck
import java.io.IOException
import javax.inject.Singleton

@Suppress("UNREACHABLE_CODE")
@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideContext(application: android.app.Application): Context {
        return application.applicationContext
    }


    @Provides
    @Singleton
    fun provideRetrofit(dataStore: MyDataStore): Retrofit {
        return Retrofit.Builder().baseUrl(URL).client(okHttpClient(dataStore))
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides

    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRequestCodeCheck(): RequestCodeCheck {
        return RequestCodeCheck()
    }

    @Provides
    @Singleton
    fun provideMyDataStore(context: Context): MyDataStore {
        return MyDataStore(context)
    }


    @SuppressLint("SuspiciousIndentation")
    fun okHttpClient(dataStore: MyDataStore): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
        var apiKey: String = ""
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.getXapiKey.collect { xApiKey ->
                Log.d("XKEY", "mainXApiKey=========>: $xApiKey")
                if (xApiKey != null) {
                    apiKey = xApiKey
                    Log.d("XKEY AFTER", "mainXApiKey=========>: $xApiKey")
                }
            }
        }
       /* okhttp.addInterceptor(Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().header("key", "DPS$951")
                    .header("x-api-key", apiKey)
                    .build()
            )
        })
        val http = okhttp.build()
        return http
*/

        okhttp.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val originals: Request = chain.request()
                try {
                    val api: String = apiKey
                    Log.e("TAG", "apikey=>$apiKey")
//
                    val requestBuilder = originals.newBuilder()
//                        .header("key", "DPS$951").header("x-api-key", api)

                    if (apiKey.isEmpty()) {
                        requestBuilder.header("key", "DPS$951")
                    } else {
                        requestBuilder.header("key", "DPS$951")
                        requestBuilder.addHeader("x-api-key", api)
                    }
                    val request = requestBuilder.build()
                    return chain.proceed(request)
                } catch (e: Exception) {
                    Log.d("DATA", "e====>${e.toString()}")
                    return okhttp3.Response.Builder()
                        .request(originals)
                        .protocol(Protocol.HTTP_1_1)
                        .code(999)
                        .message("msg")
                        .body(ResponseBody.create(null, "{${e}}")).build()
                }
            }
        })
        return okhttp.build()
    }
}


