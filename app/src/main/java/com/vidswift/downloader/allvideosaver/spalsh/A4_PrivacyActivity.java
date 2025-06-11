package com.vidswift.downloader.allvideosaver.spalsh;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.vidswift.downloader.allvideosaver.databinding.ActivityPrivacyBinding;
import com.vidswift.downloader.allvideosaver.myAds.AdsPreference;

public class A4_PrivacyActivity extends BaseActivity {

    private ActivityPrivacyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showNativeAds = false;
        toolbarHeaderText = "Privacy Policy";

        super.onCreate(savedInstanceState);

        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());

        setupToolbar(binding.toolbarLayout.headerTitle, binding.toolbarLayout.btnBack, binding.toolbarLayout.btnSettings);

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());

        String privacyUrl = AdsPreference.getPrivacyPolicyUrl();
        if (!privacyUrl.isEmpty()) {
            binding.webView.loadUrl(privacyUrl);
        } else {
            binding.webView.loadData("<h2>No Privacy Policy URL available</h2>", "text/html", "UTF-8");
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            myBackActivity();
        }
    }
}
