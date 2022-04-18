package cn.cqautotest.sunnybeach.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import cn.cqautotest.sunnybeach.R
import cn.cqautotest.sunnybeach.action.StatusAction
import cn.cqautotest.sunnybeach.aop.CheckNet
import cn.cqautotest.sunnybeach.aop.Log
import cn.cqautotest.sunnybeach.app.AppActivity
import cn.cqautotest.sunnybeach.widget.BrowserView
import cn.cqautotest.sunnybeach.widget.BrowserView.BrowserChromeClient
import cn.cqautotest.sunnybeach.widget.BrowserView.BrowserViewClient
import cn.cqautotest.sunnybeach.widget.StatusLayout
import cn.cqautotest.sunnybeach.widget.StatusLayout.OnRetryListener
import com.hjq.bar.TitleBar
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import okhttp3.FormBody
import timber.log.Timber

/**
 *    author : Android 轮子哥 & A Lonely Cat
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 浏览器界面
 */
class BrowserActivity : AppActivity(), StatusAction, OnRefreshListener {

    companion object {

        private const val INTENT_KEY_IN_URL: String = "url"

        private const val OPEN_ID: String = "id"
        private const val NICK_NAME = "nickName"
        private const val AVATAR_URL = "avatarUrl"

        // 是否加载为反馈界面
        private const val IS_FEED_BACK = "isFeedback"

        @CheckNet
        @Log
        fun start(
            context: Context,
            url: String,
            isFeedback: Boolean = false,
            openId: String = "",
            nickName: String = "",
            avatar: String = ""
        ) {
            if (TextUtils.isEmpty(url)) {
                return
            }
            val intent = Intent(context, BrowserActivity::class.java).apply {
                putExtra(INTENT_KEY_IN_URL, url)
                putExtra(IS_FEED_BACK, isFeedback)
                putExtra(OPEN_ID, openId)
                putExtra(NICK_NAME, nickName)
                putExtra(AVATAR_URL, avatar)
            }
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val hintLayout: StatusLayout? by lazy { findViewById(R.id.hl_browser_hint) }
    private val progressBar: ProgressBar? by lazy { findViewById(R.id.pb_browser_progress) }
    private val refreshLayout: SmartRefreshLayout? by lazy { findViewById(R.id.sl_browser_refresh) }
    private val browserView: BrowserView? by lazy { findViewById(R.id.wv_browser_view) }

    override fun getLayoutId(): Int {
        return R.layout.browser_activity
    }

    override fun initView() {
        // 设置 WebView 生命管控
        browserView?.setLifecycleOwner(this)
        // 设置网页刷新监听
        refreshLayout?.setOnRefreshListener(this)
    }

    override fun initData() {
        showLoading()
        browserView?.apply {
            setBrowserViewClient(AppBrowserViewClient())
            setBrowserChromeClient(AppBrowserChromeClient(this))
            val url = getString(INTENT_KEY_IN_URL)!!
            val isFeedback = getBoolean(IS_FEED_BACK)
            if (isFeedback) {
                val openId = getString(OPEN_ID)
                val nickName = getString(NICK_NAME)
                val avatar = getString(AVATAR_URL)
                Timber.d("initData：===> openId is $openId nickName is $nickName avatar is $avatar")
                val queryParams = FormBody.Builder()
                    .add("nickname", nickName ?: "游客")
                    .add("avatar", avatar ?: "")
                    .add("openid", openId ?: "")
                    .build().toQueryParams()
                postUrl(url, queryParams.toByteArray())
            } else {
                loadUrl(url)
            }
        }
    }

    private fun FormBody.toQueryParams(): String = buildString {
        repeat(size) {
            append(name(it))
            append("=")
            append(value(it))
            append("&")
        }
        if (size != 0) deleteAt(lastIndex)
    }

    override fun getStatusLayout(): StatusLayout? {
        return hintLayout
    }

    override fun onLeftClick(titleBar: TitleBar) {
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        browserView?.apply {
            if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
                // 后退网页并且拦截该事件
                goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private fun reload() {
        browserView?.reload()
    }

    /**
     * [OnRefreshListener]
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        reload()
    }

    private inner class AppBrowserViewClient : BrowserViewClient() {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post {
                showError(object : OnRetryListener {
                    override fun onRetry(layout: StatusLayout) {
                        reload()
                    }
                })
            }
        }

        /**
         * 开始加载网页
         */
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            progressBar?.visibility = View.VISIBLE
        }

        /**
         * 完成加载网页
         */
        override fun onPageFinished(view: WebView, url: String) {
            progressBar?.visibility = View.GONE
            refreshLayout?.finishRefresh()
            showComplete()
        }
    }

    private inner class AppBrowserChromeClient constructor(view: BrowserView) : BrowserChromeClient(view) {

        /**
         * 收到网页标题
         */
        override fun onReceivedTitle(view: WebView, title: String?) {
            if (title == null) {
                return
            }
            setTitle(title)
        }

        override fun onReceivedIcon(view: WebView, icon: Bitmap?) {
            if (icon == null) {
                return
            }
            setRightIcon(BitmapDrawable(resources, icon))
        }

        /**
         * 收到加载进度变化
         */
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            progressBar?.progress = newProgress
        }
    }
}