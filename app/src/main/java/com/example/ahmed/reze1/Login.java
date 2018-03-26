package com.example.ahmed.reze1;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.ahmed.reze1.GUI.CustomButton;
import com.example.ahmed.reze1.GUI.CustomEditText;
import com.example.ahmed.reze1.GUI.CustomTextView;

import com.facebook.*;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Login extends AppCompatActivity {
    CustomButton btnSignUp;
    private CustomEditText name,password;
    private CustomButton btnSignIn;
    private RequestQueue requestQueue;
    public static String URL_LOGIN = "https://rezetopia.com/app/login.php";
    private StringRequest request;
    private LoginButton fblogin;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        fblogin = (LoginButton) findViewById(R.id.login_button);
        fblogin.setReadPermissions("email");
        // If using in a fragment
        /*fblogin.setFragment(this);*/
        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Profile profile = Profile.getCurrentProfile();
                        //Toast.makeText(getBaseContext(),profile.getName(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(getBaseContext(),profile.getProfilePictureUri(200,200).toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        intent.putExtra("fbname",profile.getName());
                        intent.putExtra("fbpicurl",profile.getProfilePictureUri(200,200));
                        startActivityForResult(intent, 0);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        btnSignUp=(CustomButton)findViewById(R.id.btnRegister);
        name = (CustomEditText) findViewById(R.id.edName);
        password = (CustomEditText) findViewById(R.id.edPassword);
        btnSignIn = (CustomButton) findViewById(R.id.btnSignIn);
        requestQueue = Volley.newRequestQueue(this);
        CustomTextView tvForgetPassword=(CustomTextView)findViewById(R.id.txtvForgetPass);
        tvForgetPassword.setPaintFlags(tvForgetPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
            }
        });
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();

                request = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            //Toast.makeText(getBaseContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            if(jsonObject.getString("msg").equals("enter")){
                                //Toast.makeText(getApplicationContext(),jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                startActivityForResult(intent, 0);
                                finish();
                            }
                            else if(jsonObject.getString("msg").equals("no")){
                                Toast.makeText(getBaseContext(),R.string.wronglogin,Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getBaseContext(),response.toString(),Toast.LENGTH_LONG).show();                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("mail",name.getText().toString());
                        hashMap.put("login","login");
                        hashMap.put("password",password.getText().toString());

                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;

        String userName = name.getText().toString();
        String userPassword = password.getText().toString();

        if (userName.isEmpty()) {
            name.setError("enter your mail Address");
            valid = false;
        } else {
            name.setError(null);
        }

        if (userPassword.isEmpty() || password.length() < 4) {
            password.setError("More Than 4 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}

