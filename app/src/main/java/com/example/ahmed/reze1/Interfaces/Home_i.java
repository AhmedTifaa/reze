package com.example.ahmed.reze1.Interfaces;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


/**
 * Created by ahmed on 3/12/2018.
 */

public class Home_i {
    Context mContext;

    /** Instantiate the interface and set the context
     * @param c*/
    public Home_i(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }
}
