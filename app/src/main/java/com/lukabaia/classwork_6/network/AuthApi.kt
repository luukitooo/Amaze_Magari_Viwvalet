package com.lukabaia.classwork_6.network

import com.lukabaia.classwork_6.models.LoginForm
import com.lukabaia.classwork_6.models.RegisterForm
import com.lukabaia.classwork_6.models.UserInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    suspend fun getLoginForm(@Body userInfo: UserInfo): Response<LoginForm>

    @POST("register")
    suspend fun getRegisterForm(@Body userInfo: UserInfo): Response<RegisterForm>

}