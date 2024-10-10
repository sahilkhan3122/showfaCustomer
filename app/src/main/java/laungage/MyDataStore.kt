package laungage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MyDataStore(var context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        var FIRSTNAME = stringPreferencesKey("USER_FIRST_NAME")
        var LASTNAME = stringPreferencesKey("USER_LAST_NAME")
        var EMAIL = stringPreferencesKey("EMAIL")
        var MOBILENUMBER = stringPreferencesKey("MOBILENUMBER")
        var ADDRESS = stringPreferencesKey("ADDRESS")
        var DATEOFBIRTH = stringPreferencesKey("DATEOFBIRTH")
        var NATIONALID = stringPreferencesKey("NATIONALID")
        var REFERRALCODE = stringPreferencesKey("REFERRALCODE")
        var GENDER = stringPreferencesKey("GENDER")
        var ISLOGIN = booleanPreferencesKey("IS_LOGIN")
        var XAPIKEY = stringPreferencesKey("XAPIKEY")
        var SAVESESSIONUSER = stringPreferencesKey("SAVESESSIONUSER")
        var LATITUDE = stringPreferencesKey("LATITUDE")
        var LONGITUDE = stringPreferencesKey("LONGITUDE")
        var DEVICETYPE = stringPreferencesKey("DEVICETYPE")
        var DEVICETOKEN = stringPreferencesKey("DEVICETOKEN")
        var PASSWORD = stringPreferencesKey("PASSWORD")
        var OTP = intPreferencesKey("OTPPASSWORD")
        var PROFILE = stringPreferencesKey("PROFILE")
        var USERID = stringPreferencesKey("USERID")
    }

    suspend fun setFirstNameData(firstname: String) {
        context.dataStore.edit {
            it[FIRSTNAME] = firstname
        }
    }

    suspend fun setLastNameData(lastName: String) {
        context.dataStore.edit {
            it[LASTNAME] = lastName

        }
    }

    suspend fun setEmailData(email: String) {
        context.dataStore.edit {
            it[EMAIL] = email

        }
    }

    suspend fun setMobileData(mobileData: String) {
        context.dataStore.edit {
            it[MOBILENUMBER] = mobileData

        }
    }

    suspend fun setAddressData(address: String) {
        context.dataStore.edit {
            it[ADDRESS] = address

        }
    }

    suspend fun setDateOfBirthData(dateOfBirth: String) {
        context.dataStore.edit {
            it[DATEOFBIRTH] = dateOfBirth

        }
    }

    suspend fun setNationalData(nationalID: String) {
        context.dataStore.edit {
            it[NATIONALID] = nationalID

        }
    }

    suspend fun setReferralData(referalCode: String) {
        context.dataStore.edit {
            it[REFERRALCODE] = referalCode

        }
    }

    suspend fun setGenderData(gender: String) {
        context.dataStore.edit {
            it[GENDER] = gender

        }
    }

    suspend fun setSessionId(key: String, value: String) {
        context.dataStore.edit {
            it[SAVESESSIONUSER] = key
        }

    }

    suspend fun setLoginUser(isLogin: Boolean) {
        context.dataStore.edit {
            it[ISLOGIN] = isLogin
        }
    }

    suspend fun setUSerId(id: String) {
        context.dataStore.edit {
            it[USERID] = id
        }
    }

    suspend fun setUSerOtp(otp: Int) {
        context.dataStore.edit {
            it[OTP] = otp
        }
    }

    suspend fun setLat(lat: String) {
        context.dataStore.edit {
            it[LATITUDE] = lat
            Log.d("LATDATA==>", lat)
        }
    }

    suspend fun setLng(lng: String) {
        context.dataStore.edit {
            it[LONGITUDE] = lng
        }
    }

    suspend fun setDeviceToken(setDeviceToken: String) {
        context.dataStore.edit {
            it[DEVICETOKEN] = setDeviceToken
        }
    }

    suspend fun setDeviceType(setDeviceType: String) {
        context.dataStore.edit {
            it[DEVICETYPE] = setDeviceType
        }
    }

    suspend fun setPassword(setPassword: String) {
        context.dataStore.edit {
            it[PASSWORD] = setPassword
        }
    }

    suspend fun setProfile(setProfile: String) {
        context.dataStore.edit {
            it[PROFILE] = setProfile
        }
    }

    suspend fun setXApiKey(xApiKey: String) {
        context.dataStore.edit {
            it[XAPIKEY] = xApiKey
        }
    }


    suspend fun logoutId() {
        context.dataStore.edit {
            it.clear()
        }
    }

    val getLoginUser: Flow<Boolean> = context.dataStore.data.map {
        it[ISLOGIN] ?: false
    }
    val getUSerOtp: Flow<Int> = context.dataStore.data.map {
        it[OTP] ?: 0
    }
    val getUSerFirstName: Flow<String> = context.dataStore.data.map {
        it[FIRSTNAME] ?: ""

    }
    val getUSerlastName: Flow<String> = context.dataStore.data.map {
        it[LASTNAME] ?: ""

    }
    val getEmail: Flow<String> = context.dataStore.data.map {
        it[EMAIL] ?: ""
    }
    val getMobileNumber: Flow<String> = context.dataStore.data.map {
        it[MOBILENUMBER] ?: ""
    }
    val getAddress: Flow<String> = context.dataStore.data.map {
        it[ADDRESS] ?: ""
    }
    val getDateOfBirth: Flow<String> = context.dataStore.data.map {
        it[DATEOFBIRTH] ?: ""
    }
    val getNationalId: Flow<String> = context.dataStore.data.map {
        it[NATIONALID] ?: ""
    }
    val getReferralCode: Flow<String> = context.dataStore.data.map {
        it[REFERRALCODE] ?: ""
    }
    val getLat: Flow<String> = context.dataStore.data.map {
        it
        it[LATITUDE] ?: ""
    }
    val getLng: Flow<String> = context.dataStore.data.map {
        it[LONGITUDE] ?: ""
    }
    val getDeviceToken: Flow<String> = context.dataStore.data.map {
        it[DEVICETOKEN] ?: ""
    }
    val getType: Flow<String> = context.dataStore.data.map {
        it[DEVICETYPE] ?: ""
    }
    val getPassword: Flow<String> = context.dataStore.data.map {
        it[PASSWORD] ?: ""
    }
    val getGender: Flow<String> = context.dataStore.data.map {
        it[GENDER] ?: ""
    }

    val getUSerSession: Flow<String> = context.dataStore.data.map {
        it[SAVESESSIONUSER] ?: ""
    }
    val getProfile: Flow<String> = context.dataStore.data.map {
        it[PROFILE] ?:""
    }
    val getUSerId: Flow<String> = context.dataStore.data.map {
        it[USERID] ?: ""
    }


    val getXapiKey: Flow<String?> = context.dataStore.data.map {
        it[XAPIKEY]

    }
}