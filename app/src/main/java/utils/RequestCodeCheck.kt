package utils

import retrofit2.Response

open  class RequestCodeCheck {

    open suspend fun<T> getResponse(response: Response<T>): ShowStatus<T> {
        return if(response.code()==200){
            ShowStatus.success(response.body())
        }else{
            ShowStatus.error(response.body(), "")
        }
    }
}