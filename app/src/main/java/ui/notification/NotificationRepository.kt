package ui.notification

import model.DeletAccountModel
import model.NotificationClearModel
import model.NotificationListModel
import network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(var apiInterface: ApiInterface) {
    suspend fun notificationRepository(url:String): Response<NotificationListModel> {
        return apiInterface.notificationListApi(url)
    }
    suspend fun notificationClearRepository(url:String): Response<NotificationClearModel> {
        return apiInterface.notificationLClearApi(url)
    }

}