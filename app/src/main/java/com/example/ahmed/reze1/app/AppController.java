package com.example.ahmed.reze1.app;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.Login;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

		getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit()
				.putString(FirebaseInstanceId.getInstance().getToken(), AppConfig.DEVICE_TOKEN_SHARED)
				.apply();
		Toast.makeText(this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_LONG).show();
		Log.i("fcmtoken",  FirebaseInstanceId.getInstance().getToken());
		updateToken();
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	private void updateToken(){
		StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.18:80/reze/push_notification.php",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							boolean error = jsonObject.getBoolean("error");
							if (!error){
								Log.i("volley response", "updateToken: " + jsonObject.getString("message"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("volley error", "onErrorResponse: " + error.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<>();

				String token = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
						.getString(AppConfig.DEVICE_TOKEN_SHARED, "null");

				String userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
						.getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "null");

				map.put("method", "update_token");
				map.put("token", token);
				map.put("userId", userId);

				return map;
			}
		};

		Volley.newRequestQueue(this).add(stringRequest);
	}

}