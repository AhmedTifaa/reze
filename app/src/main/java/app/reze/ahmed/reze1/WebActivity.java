package app.reze.ahmed.reze1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_web);

        webView = new WebView(this);
        webView.loadUrl("https://docs.google.com/document/d/1kduePmoahOUDGCqy6Wst_yBb6Y0AlwcPn4mWVqqo328/edit#heading=h.w2fvndcnsesy");
        setContentView(webView);
    }
}
