package com.lukabaia.classwork_6.viewmodel

import androidx.lifecycle.ViewModel
import com.lukabaia.classwork_6.data.DataStoreHandler
import com.lukabaia.classwork_6.models.LoginForm
import com.lukabaia.classwork_6.models.UserInfo
import com.lukabaia.classwork_6.network.RetrofitInstance
import com.lukabaia.classwork_6.utils.ResponseHandler
import kotlinx.coroutines.flow.flow

class LoginViewModel: ViewModel() {

    fun getLoginFlow(userInfo: UserInfo) = flow<ResponseHandler> {
        emit(ResponseHandler.Loader(isLoading = true))
        val response = RetrofitInstance.getAuthApi().getLoginForm(userInfo)
        if (response.isSuccessful && response.body() != null) {
            emit(ResponseHandler.Success<LoginForm>(response.body()!!))
        } else {
            emit(ResponseHandler.Error(response.errorBody()?.string() ?: "Error!"))
        }
        emit(ResponseHandler.Loader(isLoading = false))
    }

    suspend fun save(key: String, value: String) {
        DataStoreHandler.save(key, value)
    }

    fun getPreferences() = DataStoreHandler.getPreferences()

}