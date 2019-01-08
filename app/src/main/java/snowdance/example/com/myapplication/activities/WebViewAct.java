package snowdance.example.com.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import snowdance.example.com.myapplication.R;
import snowdance.example.com.myapplication.utils.MLog;

/**
 * Project Name : EnjoyLife
 * Package Name : snowdance.example.com.myapplication.activities
 * FILE    Name : WebViewAct
 * Creator Name : Snow_Dance
 * Creator Time : 2018/11/11/011 16:31
 * Description  : NULL
 */

public class WebViewAct extends BaseAct{

    //  进度旋转
    private ProgressBar progressBar;
    //  网页
    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.mProgressBar);
        webView = findViewById(R.id.mWebView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url   = intent.getStringExtra("url");
        MLog.d("title: " + title);
        MLog.d("url: " + url);

        //  设置页面标题
        getSupportActionBar().setTitle(title);
        //  加载网页逻辑

        //  支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        //  支持缩放功能
        webView.getSettings().setSupportZoom(true);
        //  设置控制器
        webView.getSettings().setBuiltInZoomControls(true);
        //  接口回调
        webView.setWebChromeClient(new WebViewClient());

        //  加载网页
        webView.loadUrl(url);
        //  在本地显示而非浏览器
        webView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;   //本地浏览
            }
        });
    }

    public class WebViewClient extends WebChromeClient{

        //  进度变化监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if( newProgress == 100 ){
                progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
