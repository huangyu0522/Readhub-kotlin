package com.huangyu.readhub.ui.article

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.huangyu.readhub.R
import com.huangyu.readhub.base.BaseActivity
import kotlinx.android.synthetic.main.activity_article.*

/**
 * Created by huangyu on 2018/4/4.
 */
class ArticleActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_URL = "url"

        fun start(activity: Activity, title: String, url: String) {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_URL, url)
            activity.startActivity(intent)
        }
    }

    private lateinit var title: String
    private lateinit var url: String

    override fun getLayoutId(): Int = R.layout.activity_article

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        title = intent.getStringExtra(EXTRA_TITLE)
        url = intent.getStringExtra(EXTRA_URL)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(title)

        refresh_layout.setColorSchemeResources(R.color.colorPrimary)
        refresh_layout.setOnRefreshListener(this)

        web_view.settings.javaScriptEnabled = true
        web_view.settings.domStorageEnabled = true

        web_view.settings.pluginState = WebSettings.PluginState.ON
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            web_view.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
    }

    override fun initListeners() {
        web_view.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progress_bar.visibility = View.GONE

                refresh_layout.isRefreshing = false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && request!!.isForMainFrame) {
                    Toast.makeText(this@ArticleActivity, getString(R.string.load_err), Toast.LENGTH_SHORT).show()
                }
            }

        }
        web_view.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress_bar.progress = newProgress
            }

        }

        web_view.loadUrl(url)
    }

    override fun onRefresh() {
        web_view.reload()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        web_view.destroy()
        super.onDestroy()
    }

}
