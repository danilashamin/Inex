package ru.messenger.inex_messenger.http;

import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Алексей on 28.03.2017.
 */

public class AbstractWebView {
    public WebView getmWebView() {
        return mWebView;
    }

    public void setmWebView(WebView mWebView) {
        this.mWebView = mWebView;
    }

    private WebView mWebView = null;

    public AbstractWebView(){
        if (mWebView == null) {
            mWebView = new WebView(null);
            //mWebView.loadUrl("file:///android_asset/index.html");
            this.mWebView.getSettings().setDomStorageEnabled(true);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setWebChromeClient(new WebChromeClient());
            this.mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);

                    mWebView.loadUrl("file:///android_asset/www/ConnectionRefused.html");
                }
            });
            this.mWebView.clearCache(true);

            this.mWebView.loadUrl("http://192.168.1.146/index.html");
        }
    }
}
