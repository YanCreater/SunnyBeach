package com.example.blogsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.blogsystem.R
import com.example.blogsystem.base.App
import com.example.blogsystem.execption.LoginFailedException
import com.example.blogsystem.model.BaseResponse
import com.example.blogsystem.model.User
import com.example.blogsystem.model.UserBasicInfo
import com.example.blogsystem.network.ServiceCreator
import com.example.blogsystem.network.api.UserApi
import com.example.blogsystem.utils.*
import com.example.blogsystem.viewmodel.login.LoggedInUserView
import com.example.blogsystem.viewmodel.login.LoginFormState
import com.example.blogsystem.viewmodel.login.LoginResult
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val cookieDao = getApplication<App>().database.cookieDao()

    private val _autoLogin = MutableLiveData<Boolean>()
    val autoLogin: LiveData<Boolean> get() = _autoLogin

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> get() = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _userBasicInfo = MutableLiveData<UserBasicInfo>()
    val userBasicInfo: LiveData<UserBasicInfo> get() = _userBasicInfo

    private val userApi by lazy { ServiceCreator.create<UserApi>() }

    fun login(userAccount: String, password: String, captcha: String) = viewModelScope.launch {
        val available = withContext(Dispatchers.IO) { NetworkUtils.isAvailable() }
        if (available.not()) return@launch
        val context = getApplication<Application>()
        var userBasicInfoResponse: BaseResponse<UserBasicInfo>? = null
        runCatching {
            val user = User(
                phoneNum = userAccount, password = password.md5
                    .toLowerCase(Locale.SIMPLIFIED_CHINESE)
            )
            val loginResult = userApi.login(captcha = captcha, user = user)
            val tempUserBasicInfoResponse = userApi.checkToken()
            val tempUserBasicInfoResponseData = tempUserBasicInfoResponse.data
            // 只有Token校验成功获取到数据才算登录成功，否则也是属于登录失败了
            if (SUNNY_BEACH_HTTP_OK_CODE == loginResult.code && SUNNY_BEACH_HTTP_OK_CODE == tempUserBasicInfoResponse.code) {
                userBasicInfoResponse = tempUserBasicInfoResponse
                _userBasicInfo.value = tempUserBasicInfoResponseData
                loginResult
            } else {
                throw LoginFailedException()
            }
        }.onSuccess {
            _loginResult.value =
                LoginResult(
                    success = LoggedInUserView(
                        displayName = userBasicInfoResponse?.data?.nickname ?: ""
                    )
                )
        }.onFailure {
            ToastUtils.showLong(it.toString())
            _loginResult.value =
                LoginResult(error = context.getString(R.string.login_failed))
        }
    }

    fun checkUserToken() = viewModelScope.launch {
        val available = withContext(Dispatchers.IO) { NetworkUtils.isAvailable() }
        if (available.not()) {
            if (getUserBasicInfo() != null) {
                _autoLogin.value = true
            }
            return@launch
        }
        runCatching {
            userApi.checkToken()
        }.onSuccess { response ->
            val userBasicInfo = response.data
            val loginSuccess = SUNNY_BEACH_HTTP_OK_CODE == response.code
            _autoLogin.value = loginSuccess
            _userBasicInfo.value = userBasicInfo
            if (loginSuccess) {
                saveUserBasicInfo(userBasicInfo)
            }
        }.onFailure {
            _autoLogin.value = false
        }
    }

    fun loginDataChanged(userAccount: String, password: String, captcha: String) {
        _loginForm.value = when {
            isUserAccountValid(userAccount).not() -> LoginFormState(userAccountError = R.string.invalid_username)
            isPasswordValid(password).not() -> LoginFormState(passwordError = R.string.invalid_password)
            isVerifyCodeValid(captcha).not() -> LoginFormState(verifyError = R.string.invalid_verify_code)
            else -> LoginFormState(isDataValid = true)
        }
    }

    fun checkUserState() = viewModelScope.launch {
        val available = withContext(Dispatchers.IO) { NetworkUtils.isAvailable() }
        if (available.not()) return@launch
        val userBasicInfoResponse = userApi.checkToken()
        val userBasicInfoResponseData = userBasicInfoResponse.data
        if (SUNNY_BEACH_HTTP_OK_CODE == userBasicInfoResponse.code) {
            saveUserBasicInfo(userBasicInfoResponseData)
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = userBasicInfoResponseData.nickname))
        }
    }

    /**
     * 保存用户基本信息
     */
    private fun saveUserBasicInfo(userBasicInfo: UserBasicInfo) {
        val mmkv = MMKV.defaultMMKV() ?: return
        mmkv.putString(SUNNY_BEACH_USER_BASIC_INFO, userBasicInfo.toJson())
    }

    /**
     * 获取用户基本信息
     */
    private fun getUserBasicInfo(): UserBasicInfo? {
        val mmkv = MMKV.defaultMMKV() ?: return null
        return runCatching {
            val jsonByUserBasicInfo = mmkv.getString(SUNNY_BEACH_USER_BASIC_INFO, null)
            GsonUtils.fromJson(jsonByUserBasicInfo, UserBasicInfo::class.java)
        }.getOrNull()
    }

    /**
     * 手机号码格式检查
     */
    private fun isUserAccountValid(userAccount: String): Boolean =
        RegexUtils.isMobileExact(userAccount)

    /**
     * 账号密码长度检查
     */
    private fun isPasswordValid(password: String): Boolean = password.length > 5

    /**
     * 验证码检查
     */
    private fun isVerifyCodeValid(verifyCode: String): Boolean = verifyCode.isNotEmpty()
}