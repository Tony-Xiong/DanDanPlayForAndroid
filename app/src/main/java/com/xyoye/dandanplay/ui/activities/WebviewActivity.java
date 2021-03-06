package com.xyoye.dandanplay.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.xyoye.dandanplay.R;
import com.xyoye.dandanplay.ui.weight.IWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xyy on 2018/5/22.
 */

public class WebviewActivity extends AppCompatActivity {
    @BindView(R.id.i_webview)
    IWebView IwebView;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    private boolean isSelectUrl = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color), 0);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String link = intent.getStringExtra("link");
        isSelectUrl = intent.getBooleanExtra("isSelect", false);
        setTitle(title);
        IwebView.loadUrl(link);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                WebviewActivity.this.finish();
                break;
            case R.id.select_url:
                if(!StringUtils.isEmpty(IwebView.getUrl())){
                    Intent intent = getIntent();
                    intent.putExtra("selectUrl", IwebView.getUrl());
                    setResult(DownloadBilibiliActivity.SELECT_WEB, intent);
                    WebviewActivity.this.finish();
                }else {
                    ToastUtils.showShort("url不能为空");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isSelectUrl)
            getMenuInflater().inflate(R.menu.menu_url_select, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        IwebView.onPause();
        IwebView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        IwebView.onResume();
        IwebView.resumeTimers();
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && IwebView.canGoBack()) {
            IwebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
