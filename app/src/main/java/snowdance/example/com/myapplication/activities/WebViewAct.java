package snowdance.example.com.myapplication.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
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
        final String title = intent.getStringExtra("title");
        final String url   = intent.getStringExtra("url");
        final String img   = intent.getStringExtra("img");
        MLog.d("title: " + title);
        MLog.d("url: " + url);
        MLog.d("img: " + img);

        WebSettings webSettings = webView.getSettings();
//        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
//            webView.getSettings()
//                    .setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }

        //  设置页面标题
        getSupportActionBar().setTitle(title);
        //  加载网页逻辑

        //  支持JavaScript
        webSettings.setJavaScriptEnabled(true);
//        //  JS打开窗口
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //  支持缩放功能
        webSettings.setSupportZoom(true);
//        //  任意比例缩放
//        webSettings.setUseWideViewPort(true);
//        //  自适应
//        webSettings.setLoadWithOverviewMode(true);
        //  设置控制器
        webSettings.setBuiltInZoomControls(true);
//        //  接口回调
        webView.setWebChromeClient(new WebViewClient());
//        //  支持H5标签
//        webSettings.setDomStorageEnabled(true);
//        //  本地DOM缓存
//        webSettings.setCacheMode(webView.getSettings().LOAD_CACHE_ELSE_NETWORK);
//        //  缓存
//        webSettings.setAppCacheEnabled(true);//是否使用缓存
//        //  图片
//        webSettings.setLoadsImagesAutomatically(true);

        //  加载网页
//        webView.loadUrl(url);
        webView.loadDataWithBaseURL(null, url, "text/html",
                "utf-8", null);
        //  在本地显示而非浏览器
//        webView.setWebViewClient(new android.webkit.WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(url);
//                return true;   //本地浏览
//            }
//        });
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
