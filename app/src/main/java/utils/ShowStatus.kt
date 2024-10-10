package utils

import com.example.utils.Status


data class ShowStatus<out T>(val status: Status, val data: T?) {

    companion object{

         fun<T>loading(data:T?): ShowStatus<T>? {
              return ShowStatus(Status.LOADING, data)
         }
        fun<T>success(data:T?): ShowStatus<T> {
            return ShowStatus(Status.SUCCESS, data)
        }
        fun<T>error(data:T?,message:String): ShowStatus<T> {
            return ShowStatus(Status.ERROR, data)
        }
    }
}