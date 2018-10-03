package ru.messenger.inex_messenger.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import ru.messenger.inex_messenger.R;

/**
 * Created by Алексей on 21.03.2017.
 */

public class VotingActivity  extends XmppActivity {
    protected FrameLayout mWebViewPlaceholder;
    private WebView mWebView = null;

    @Override
    protected void refreshUiReal() {

    }

    @Override
    void onBackendConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voting);//activity_voting);
        initUI();
    }

    protected void initUI() {
        mWebViewPlaceholder = ((FrameLayout)findViewById(R.id.webViewPlaceholder));

        if (mWebView == null) {
            mWebView = new WebView(this);
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

            this.mWebView.loadUrl("http://ukraft.ru/vote/index.html");

        }
        mWebViewPlaceholder.addView(mWebView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mWebView != null) {
            mWebViewPlaceholder.removeView(mWebView); // перед сменой конфига выдергиваем WebView из UI
        }
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_voting);
        initUI(); // возвращаем WebView обратно в UI
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }


}
