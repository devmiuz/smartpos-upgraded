package uz.uzkassa.common.feature.browser.presentation.delegate

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ViewDelegate
import java.lang.ref.WeakReference

internal class WebViewDelegate(
    fragment: Fragment,
    private val listener: OnWebViewStateChangedListener
) : ViewDelegate<WebView>(fragment, fragment) {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(view: WebView, savedInstanceState: Bundle?) {
        super.onCreate(view, savedInstanceState)
        view.webViewClient = CustomWebViewClient(listener)
        view.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
            domStorageEnabled = true
        }
    }

    fun loadUrl(url: String) {
        view?.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.destroy()
    }

    override fun onResume() {
        super.onResume()
        view?.onResume()
    }

    override fun onPause() {
        view?.onPause()
        super.onPause()
    }

    interface OnWebViewStateChangedListener {
        fun onStartPageLoading()
        fun onFinishPageLoading()
    }

    private class CustomWebViewClient(
        listener: OnWebViewStateChangedListener
    ) : WebViewClientCompat() {
        private val reference: WeakReference<OnWebViewStateChangedListener> =
            WeakReference(listener)

        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            view?.loadUrl(url)
            return false
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceErrorCompat
        ) {
            view.reload()
        }

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            view?.reload()
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            view?.reload()
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            view.reload()
        }

        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
            reference.get()?.onStartPageLoading()
        }

        override fun onPageFinished(view: WebView, url: String?) {
            if (view.certificate == null) view.reload()
            else reference.get()?.onFinishPageLoading()
        }
    }
}